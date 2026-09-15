package com.example.presentation.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.AppContainer
import com.example.domain.model.ProjectSummary
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ProjectSortOrder {
    NEWEST,
    OLDEST,
    ALPHABETICAL
}

data class ProjectsUiState(
    val projects: List<ProjectSummary> = emptyList(),
    val searchQuery: String = "",
    val sortOrder: ProjectSortOrder = ProjectSortOrder.NEWEST,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ProjectsUiEvent {
    data class ShowSnackbar(val message: String) : ProjectsUiEvent()
    data class ShareProjectText(val text: String) : ProjectsUiEvent()
}

class ProjectsViewModel(
    private val container: AppContainer
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortOrder = MutableStateFlow(ProjectSortOrder.NEWEST)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProjectsUiEvent>()
    val events: SharedFlow<ProjectsUiEvent> = _events.asSharedFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _isLoading.value = true
            combine(
                container.getProjectsUseCase(),
                _searchQuery,
                _sortOrder
            ) { allProjects, query, sort ->
                val filtered = if (query.isBlank()) {
                    allProjects
                } else {
                    allProjects.filter { it.name.contains(query.trim(), ignoreCase = true) }
                }

                val sorted = when (sort) {
                    ProjectSortOrder.NEWEST -> filtered.sortedByDescending { it.updatedAt }
                    ProjectSortOrder.OLDEST -> filtered.sortedBy { it.createdAt }
                    ProjectSortOrder.ALPHABETICAL -> filtered.sortedBy { it.name }
                }
                sorted
            }.collect { sortedProjects ->
                _uiState.value = _uiState.value.copy(
                    projects = sortedProjects,
                    searchQuery = _searchQuery.value,
                    sortOrder = _sortOrder.value,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onSortOrderChanged(order: ProjectSortOrder) {
        _sortOrder.value = order
        _uiState.value = _uiState.value.copy(sortOrder = order)
    }

    fun addProject(name: String, onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            val result = container.addProjectUseCase(name)
            result.onSuccess { id ->
                _events.emit(ProjectsUiEvent.ShowSnackbar("تم إنشاء المشروع بنجاح"))
                onSuccess(id)
            }.onFailure { error ->
                _events.emit(ProjectsUiEvent.ShowSnackbar(error.message ?: "حدث خطأ أثناء الإضافة"))
            }
        }
    }

    fun updateProjectName(id: Long, newName: String) {
        viewModelScope.launch {
            val result = container.updateProjectUseCase(id, newName)
            result.onSuccess {
                _events.emit(ProjectsUiEvent.ShowSnackbar("تم تعديل اسم المشروع بنجاح"))
            }.onFailure { error ->
                _events.emit(ProjectsUiEvent.ShowSnackbar(error.message ?: "حدث خطأ أثناء التعديل"))
            }
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            val result = container.deleteProjectUseCase(id)
            result.onSuccess {
                _events.emit(ProjectsUiEvent.ShowSnackbar("تم حذف المشروع بنجاح"))
            }.onFailure { error ->
                _events.emit(ProjectsUiEvent.ShowSnackbar(error.message ?: "حدث خطأ أثناء الحذف"))
            }
        }
    }

    fun duplicateProject(projectId: Long) {
        viewModelScope.launch {
            val result = container.duplicateProjectUseCase(projectId)
            result.onSuccess {
                _events.emit(ProjectsUiEvent.ShowSnackbar("تم تكرار المشروع بنجاح"))
            }.onFailure { error ->
                _events.emit(ProjectsUiEvent.ShowSnackbar(error.message ?: "حدث خطأ أثناء النسخ"))
            }
        }
    }

    fun shareProjectSummary(project: ProjectSummary) {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val dateStr = dateFormat.format(Date(project.updatedAt))
            val shareContent = buildString {
                appendLine("⚡ ملخص مشروع كهربائي ⚡")
                appendLine("المشروع: ${project.name}")
                appendLine("تاريخ التعديل: $dateStr")
                appendLine("عدد القطاعات: ${project.sectorCount}")
                appendLine("إجمالي أصناف المواد: ${project.totalItemsCount}")
                appendLine("إجمالي الكميات: ${project.totalQuantity}")
            }
            _events.emit(ProjectsUiEvent.ShareProjectText(shareContent))
        }
    }

    fun exportBackup(onExported: (String) -> Unit) {
        viewModelScope.launch {
            val json = container.exportBackupUseCase()
            onExported(json)
        }
    }

    fun importBackup(json: String) {
        viewModelScope.launch {
            val success = container.importBackupUseCase(json)
            if (success) {
                _events.emit(ProjectsUiEvent.ShowSnackbar("تم استعادة النسخة الاحتياطية بنجاح"))
            } else {
                _events.emit(ProjectsUiEvent.ShowSnackbar("فشل في استيراد البيانات، تأكد من صحة الملف"))
            }
        }
    }
}
