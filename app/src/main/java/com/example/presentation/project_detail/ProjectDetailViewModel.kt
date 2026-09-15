package com.example.presentation.project_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.di.AppContainer
import com.example.domain.model.Project
import com.example.domain.model.SectorSummary
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ProjectDetailUiState(
    val project: Project? = null,
    val sectors: List<SectorSummary> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

sealed class ProjectDetailUiEvent {
    data class ShowSnackbar(val message: String) : ProjectDetailUiEvent()
}

class ProjectDetailViewModel(
    private val projectId: Long,
    private val container: AppContainer
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectDetailUiState())
    val uiState: StateFlow<ProjectDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProjectDetailUiEvent>()
    val events: SharedFlow<ProjectDetailUiEvent> = _events.asSharedFlow()

    init {
        loadProjectDetails()
    }

    private fun loadProjectDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            launch {
                container.getProjectUseCase(projectId).collectLatest { project ->
                    _uiState.value = _uiState.value.copy(project = project, isLoading = false)
                }
            }

            launch {
                container.getSectorsUseCase(projectId).collectLatest { sectors ->
                    _uiState.value = _uiState.value.copy(sectors = sectors, isLoading = false)
                }
            }
        }
    }

    fun addSector(name: String, onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            val result = container.addSectorUseCase(projectId, name)
            result.onSuccess { sectorId ->
                _events.emit(ProjectDetailUiEvent.ShowSnackbar("تم إضافة القطاع بنجاح"))
                onSuccess(sectorId)
            }.onFailure { error ->
                _events.emit(ProjectDetailUiEvent.ShowSnackbar(error.message ?: "فشل في إضافة القطاع"))
            }
        }
    }

    fun updateSectorName(sectorId: Long, newName: String) {
        viewModelScope.launch {
            val result = container.updateSectorUseCase(sectorId, newName)
            result.onSuccess {
                _events.emit(ProjectDetailUiEvent.ShowSnackbar("تم تعديل اسم القطاع"))
            }.onFailure { error ->
                _events.emit(ProjectDetailUiEvent.ShowSnackbar(error.message ?: "فشل في تعديل القطاع"))
            }
        }
    }

    fun deleteSector(sectorId: Long) {
        viewModelScope.launch {
            val result = container.deleteSectorUseCase(sectorId)
            result.onSuccess {
                _events.emit(ProjectDetailUiEvent.ShowSnackbar("تم حذف القطاع بنجاح"))
            }.onFailure { error ->
                _events.emit(ProjectDetailUiEvent.ShowSnackbar(error.message ?: "فشل في حذف القطاع"))
            }
        }
    }
}
