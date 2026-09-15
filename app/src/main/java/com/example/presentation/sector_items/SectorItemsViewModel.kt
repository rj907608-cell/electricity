package com.example.presentation.sector_items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.AppContainer
import com.example.domain.model.MaterialItem
import com.example.domain.model.SectorSupplyItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class MaterialItemUiModel(
    val materialId: Long,
    val name: String,
    val unit: String,
    val quantity: Int
)

data class SectorItemsUiState(
    val materials: List<MaterialItemUiModel> = emptyList(),
    val searchQuery: String = "",
    val totalSelectedItems: Int = 0,
    val totalQuantity: Int = 0,
    val isLoading: Boolean = true
)

sealed class SectorItemsUiEvent {
    data class ShowSnackbar(val message: String) : SectorItemsUiEvent()
}

class SectorItemsViewModel(
    val sectorId: Long,
    val sectorName: String,
    private val container: AppContainer
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _materialsQuantities = MutableStateFlow<Map<Long, Int>>(emptyMap())
    private val _allMaterials = MutableStateFlow<List<MaterialItem>>(emptyList())

    private val _uiState = MutableStateFlow(SectorItemsUiState())
    val uiState: StateFlow<SectorItemsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SectorItemsUiEvent>()
    val events: SharedFlow<SectorItemsUiEvent> = _events.asSharedFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load existing supplies for this sector
            val existingSupplies = container.getSectorSuppliesUseCase(sectorId).first()
            val initialMap = existingSupplies.associate { it.materialId to it.quantity }
            _materialsQuantities.value = initialMap

            launch {
                container.getAllMaterialsUseCase().collect { materials ->
                    _allMaterials.value = materials
                }
            }

            combine(_allMaterials, _materialsQuantities, _searchQuery) { materials, quantities, query ->
                val filtered = if (query.isBlank()) {
                    materials
                } else {
                    materials.filter { it.name.contains(query.trim(), ignoreCase = true) }
                }

                val uiModels = filtered.map { mat ->
                    MaterialItemUiModel(
                        materialId = mat.id,
                        name = mat.name,
                        unit = mat.unit,
                        quantity = quantities[mat.id] ?: 0
                    )
                }

                val selectedCount = quantities.count { it.value > 0 }
                val totalQty = quantities.values.filter { it > 0 }.sum()

                SectorItemsUiState(
                    materials = uiModels,
                    searchQuery = query,
                    totalSelectedItems = selectedCount,
                    totalQuantity = totalQty,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun incrementQuantity(materialId: Long) {
        val current = _materialsQuantities.value[materialId] ?: 0
        setQuantity(materialId, current + 1)
    }

    fun decrementQuantity(materialId: Long) {
        val current = _materialsQuantities.value[materialId] ?: 0
        if (current > 0) {
            setQuantity(materialId, current - 1)
        }
    }

    fun setQuantity(materialId: Long, newQuantity: Int) {
        val coerced = newQuantity.coerceAtLeast(0)
        val updated = _materialsQuantities.value.toMutableMap()
        if (coerced == 0) {
            updated.remove(materialId)
        } else {
            updated[materialId] = coerced
        }
        _materialsQuantities.value = updated

        // Persist immediately in Room database
        viewModelScope.launch {
            container.updateSectorItemQuantityUseCase(sectorId, materialId, coerced)
        }
    }
}
