package com.example.di

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.repository.ElectricRepositoryImpl
import com.example.domain.repository.ElectricRepository
import com.example.domain.usecase.AddProjectUseCase
import com.example.domain.usecase.AddSectorUseCase
import com.example.domain.usecase.DeleteProjectUseCase
import com.example.domain.usecase.DeleteSectorUseCase
import com.example.domain.usecase.DuplicateProjectUseCase
import com.example.domain.usecase.ExportBackupUseCase
import com.example.domain.usecase.GetAllMaterialsUseCase
import com.example.domain.usecase.GetProjectInventoryUseCase
import com.example.domain.usecase.GetProjectUseCase
import com.example.domain.usecase.GetProjectsUseCase
import com.example.domain.usecase.GetSectorSuppliesUseCase
import com.example.domain.usecase.GetSectorUseCase
import com.example.domain.usecase.GetSectorsUseCase
import com.example.domain.usecase.ImportBackupUseCase
import com.example.domain.usecase.SearchMaterialsUseCase
import com.example.domain.usecase.SearchProjectsUseCase
import com.example.domain.usecase.UpdateProjectUseCase
import com.example.domain.usecase.UpdateSectorItemQuantityUseCase
import com.example.domain.usecase.UpdateSectorUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface AppContainer {
    val repository: ElectricRepository
    val getProjectsUseCase: GetProjectsUseCase
    val searchProjectsUseCase: SearchProjectsUseCase
    val getProjectUseCase: GetProjectUseCase
    val addProjectUseCase: AddProjectUseCase
    val updateProjectUseCase: UpdateProjectUseCase
    val deleteProjectUseCase: DeleteProjectUseCase
    val duplicateProjectUseCase: DuplicateProjectUseCase
    val getSectorsUseCase: GetSectorsUseCase
    val getSectorUseCase: GetSectorUseCase
    val addSectorUseCase: AddSectorUseCase
    val updateSectorUseCase: UpdateSectorUseCase
    val deleteSectorUseCase: DeleteSectorUseCase
    val getAllMaterialsUseCase: GetAllMaterialsUseCase
    val searchMaterialsUseCase: SearchMaterialsUseCase
    val getSectorSuppliesUseCase: GetSectorSuppliesUseCase
    val updateSectorItemQuantityUseCase: UpdateSectorItemQuantityUseCase
    val getProjectInventoryUseCase: GetProjectInventoryUseCase
    val exportBackupUseCase: ExportBackupUseCase
    val importBackupUseCase: ImportBackupUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context, applicationScope)
    }

    override val repository: ElectricRepository by lazy {
        val repo = ElectricRepositoryImpl(
            projectDao = database.projectDao(),
            sectorDao = database.sectorDao(),
            materialDao = database.materialDao(),
            sectorItemDao = database.sectorItemDao()
        )
        applicationScope.launch {
            repo.ensureDefaultMaterials()
        }
        repo
    }

    override val getProjectsUseCase by lazy { GetProjectsUseCase(repository) }
    override val searchProjectsUseCase by lazy { SearchProjectsUseCase(repository) }
    override val getProjectUseCase by lazy { GetProjectUseCase(repository) }
    override val addProjectUseCase by lazy { AddProjectUseCase(repository) }
    override val updateProjectUseCase by lazy { UpdateProjectUseCase(repository) }
    override val deleteProjectUseCase by lazy { DeleteProjectUseCase(repository) }
    override val duplicateProjectUseCase by lazy { DuplicateProjectUseCase(repository) }
    override val getSectorsUseCase by lazy { GetSectorsUseCase(repository) }
    override val getSectorUseCase by lazy { GetSectorUseCase(repository) }
    override val addSectorUseCase by lazy { AddSectorUseCase(repository) }
    override val updateSectorUseCase by lazy { UpdateSectorUseCase(repository) }
    override val deleteSectorUseCase by lazy { DeleteSectorUseCase(repository) }
    override val getAllMaterialsUseCase by lazy { GetAllMaterialsUseCase(repository) }
    override val searchMaterialsUseCase by lazy { SearchMaterialsUseCase(repository) }
    override val getSectorSuppliesUseCase by lazy { GetSectorSuppliesUseCase(repository) }
    override val updateSectorItemQuantityUseCase by lazy { UpdateSectorItemQuantityUseCase(repository) }
    override val getProjectInventoryUseCase by lazy { GetProjectInventoryUseCase(repository) }
    override val exportBackupUseCase by lazy { ExportBackupUseCase(repository) }
    override val importBackupUseCase by lazy { ImportBackupUseCase(repository) }
}
