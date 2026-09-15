package com.example.presentation.inventory

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.AppContainer
import com.example.domain.model.ProjectInventory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class InventoryUiState(
    val inventory: ProjectInventory? = null,
    val isBreakdownExpanded: Boolean = false,
    val isLoading: Boolean = true
)

sealed class InventoryUiEvent {
    data class ShowSnackbar(val message: String) : InventoryUiEvent()
    data class ShareInventoryText(val text: String) : InventoryUiEvent()
}

class InventoryViewModel(
    val projectId: Long,
    private val container: AppContainer
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<InventoryUiEvent>()
    val events: SharedFlow<InventoryUiEvent> = _events.asSharedFlow()

    init {
        loadInventory()
    }

    private fun loadInventory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            container.getProjectInventoryUseCase(projectId).collectLatest { inventory ->
                _uiState.value = _uiState.value.copy(
                    inventory = inventory,
                    isLoading = false
                )
            }
        }
    }

    fun toggleBreakdownExpanded() {
        _uiState.value = _uiState.value.copy(
            isBreakdownExpanded = !_uiState.value.isBreakdownExpanded
        )
    }

    fun generateFormattedReport(): String {
        val inv = _uiState.value.inventory ?: return ""
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date())

        return buildString {
            appendLine("📋 ══════════════════════════════ 📋")
            appendLine("       فاتورة وجرد المواد الكهربائية       ")
            appendLine("📋 ══════════════════════════════ 📋")
            appendLine("المشروع: ${inv.project.name}")
            appendLine("تاريخ الجرد: $dateStr")
            appendLine("إجمالي أصناف المواد: ${inv.grandTotalItems} صنف")
            appendLine("إجمالي مجموع الكميات: ${inv.grandTotalQuantity} وحدة")
            appendLine("----------------------------------")
            appendLine("📦 إجمالي المواد المطلوبة عبر المشروع:")
            inv.totals.forEachIndexed { index, item ->
                appendLine("${index + 1}. ${item.materialName}: ${item.totalQuantity} (${item.unit})")
            }
            if (inv.sectorBreakdowns.isNotEmpty()) {
                appendLine("----------------------------------")
                appendLine("🚪 تفصيل المواد حسب كل قطاع:")
                inv.sectorBreakdowns.forEach { sector ->
                    appendLine("\n[ ${sector.sectorName} ]:")
                    sector.items.forEach { item ->
                        appendLine("  - ${item.materialName}: ${item.quantity} ${item.unit}")
                    }
                }
            }
            appendLine("══════════════════════════════════")
        }
    }

    fun shareInventory() {
        viewModelScope.launch {
            val report = generateFormattedReport()
            if (report.isNotEmpty()) {
                _events.emit(InventoryUiEvent.ShareInventoryText(report))
            }
        }
    }

    fun onCopiedToClipboard() {
        viewModelScope.launch {
            _events.emit(InventoryUiEvent.ShowSnackbar("تم نسخ الفاتورة والجرد إلى الحافظة بنجاح"))
        }
    }
}
