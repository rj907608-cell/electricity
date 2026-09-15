package com.example.domain.usecase

import com.example.domain.model.MaterialItem
import com.example.domain.model.Project
import com.example.domain.model.ProjectInventory
import com.example.domain.model.ProjectSummary
import com.example.domain.model.Sector
import com.example.domain.model.SectorSummary
import com.example.domain.model.SectorSupplyItem
import com.example.domain.repository.ElectricRepository
import kotlinx.coroutines.flow.Flow

class GetProjectsUseCase(private val repository: ElectricRepository) {
    operator fun invoke(): Flow<List<ProjectSummary>> = repository.getProjectSummaries()
}

class SearchProjectsUseCase(private val repository: ElectricRepository) {
    operator fun invoke(query: String): Flow<List<ProjectSummary>> =
        if (query.isBlank()) repository.getProjectSummaries()
        else repository.searchProjectSummaries(query)
}

class GetProjectUseCase(private val repository: ElectricRepository) {
    operator fun invoke(id: Long): Flow<Project?> = repository.getProject(id)
}

class AddProjectUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(name: String): Result<Long> {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("اسم المشروع لا يمكن أن يكون فارغاً"))
        }
        if (trimmed.length > 60) {
            return Result.failure(IllegalArgumentException("اسم المشروع يجب ألا يتجاوز 60 حرفاً"))
        }
        return try {
            val id = repository.addProject(trimmed)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class UpdateProjectUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(id: Long, name: String): Result<Unit> {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("اسم المشروع لا يمكن أن يكون فارغاً"))
        }
        if (trimmed.length > 60) {
            return Result.failure(IllegalArgumentException("اسم المشروع يجب ألا يتجاوز 60 حرفاً"))
        }
        return try {
            repository.updateProject(id, trimmed)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class DeleteProjectUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(id: Long): Result<Unit> = try {
        repository.deleteProject(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class DuplicateProjectUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(projectId: Long): Result<Long> = try {
        val id = repository.duplicateProject(projectId)
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class GetSectorsUseCase(private val repository: ElectricRepository) {
    operator fun invoke(projectId: Long): Flow<List<SectorSummary>> =
        repository.getSectorSummaries(projectId)
}

class GetSectorUseCase(private val repository: ElectricRepository) {
    operator fun invoke(sectorId: Long): Flow<Sector?> =
        repository.getSector(sectorId)
}

class AddSectorUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(projectId: Long, name: String): Result<Long> {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("اسم القطاع لا يمكن أن يكون فارغاً"))
        }
        return try {
            val id = repository.addSector(projectId, trimmed)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class UpdateSectorUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(sectorId: Long, name: String): Result<Unit> {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("اسم القطاع لا يمكن أن يكون فارغاً"))
        }
        return try {
            repository.updateSector(sectorId, trimmed)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class DeleteSectorUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(sectorId: Long): Result<Unit> = try {
        repository.deleteSector(sectorId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class GetAllMaterialsUseCase(private val repository: ElectricRepository) {
    operator fun invoke(): Flow<List<MaterialItem>> = repository.getAllMaterials()
}

class SearchMaterialsUseCase(private val repository: ElectricRepository) {
    operator fun invoke(query: String): Flow<List<MaterialItem>> =
        if (query.isBlank()) repository.getAllMaterials()
        else repository.searchMaterials(query)
}

class GetSectorSuppliesUseCase(private val repository: ElectricRepository) {
    operator fun invoke(sectorId: Long): Flow<List<SectorSupplyItem>> =
        repository.getSectorSupplies(sectorId)
}

class UpdateSectorItemQuantityUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(sectorId: Long, materialId: Long, quantity: Int) {
        repository.updateSectorItemQuantity(sectorId, materialId, quantity.coerceAtLeast(0))
    }
}

class GetProjectInventoryUseCase(private val repository: ElectricRepository) {
    operator fun invoke(projectId: Long): Flow<ProjectInventory?> =
        repository.getProjectInventory(projectId)
}

class ExportBackupUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(): String = repository.exportBackupJson()
}

class ImportBackupUseCase(private val repository: ElectricRepository) {
    suspend operator fun invoke(json: String): Boolean = repository.importBackupJson(json)
}
