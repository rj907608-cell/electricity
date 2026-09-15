package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.dao.MaterialDao
import com.example.data.local.dao.ProjectDao
import com.example.data.local.dao.SectorDao
import com.example.data.local.dao.SectorItemDao
import com.example.data.local.entity.MaterialEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.SectorEntity
import com.example.data.local.entity.SectorItemEntity
import com.example.domain.model.MaterialItem
import com.example.domain.model.MaterialTotal
import com.example.domain.model.Project
import com.example.domain.model.ProjectInventory
import com.example.domain.model.ProjectSummary
import com.example.domain.model.Sector
import com.example.domain.model.SectorBreakdown
import com.example.domain.model.SectorSummary
import com.example.domain.model.SectorSupplyItem
import com.example.domain.repository.ElectricRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ElectricRepositoryImpl(
    private val projectDao: ProjectDao,
    private val sectorDao: SectorDao,
    private val materialDao: MaterialDao,
    private val sectorItemDao: SectorItemDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectricRepository {

    suspend fun ensureDefaultMaterials() {
        withContext(ioDispatcher) {
            AppDatabase.populateDefaultMaterials(materialDao)
        }
    }

    override fun getProjectSummaries(): Flow<List<ProjectSummary>> {
        return projectDao.getProjectSummaries().map { list ->
            list.map { entity ->
                ProjectSummary(
                    id = entity.id,
                    name = entity.name,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt,
                    sectorCount = entity.sectorCount,
                    totalItemsCount = entity.totalItemsCount,
                    totalQuantity = entity.totalQuantity
                )
            }
        }
    }

    override fun searchProjectSummaries(query: String): Flow<List<ProjectSummary>> {
        return projectDao.searchProjectSummaries(query).map { list ->
            list.map { entity ->
                ProjectSummary(
                    id = entity.id,
                    name = entity.name,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt,
                    sectorCount = entity.sectorCount,
                    totalItemsCount = entity.totalItemsCount,
                    totalQuantity = entity.totalQuantity
                )
            }
        }
    }

    override fun getProject(id: Long): Flow<Project?> {
        return projectDao.getProjectById(id).map { entity ->
            entity?.let {
                Project(
                    id = it.id,
                    name = it.name,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
        }
    }

    override suspend fun addProject(name: String): Long = withContext(ioDispatcher) {
        val now = System.currentTimeMillis()
        val project = ProjectEntity(name = name.trim(), createdAt = now, updatedAt = now)
        projectDao.insertProject(project)
    }

    override suspend fun updateProject(id: Long, name: String) = withContext(ioDispatcher) {
        val existing = projectDao.getProjectByIdDirect(id)
        if (existing != null) {
            val updated = existing.copy(name = name.trim(), updatedAt = System.currentTimeMillis())
            projectDao.updateProject(updated)
        }
    }

    override suspend fun deleteProject(id: Long) = withContext(ioDispatcher) {
        projectDao.deleteProjectById(id)
    }

    override suspend fun duplicateProject(projectId: Long): Long = withContext(ioDispatcher) {
        val originalProject = projectDao.getProjectByIdDirect(projectId) ?: return@withContext -1L
        val now = System.currentTimeMillis()
        val newProject = ProjectEntity(
            name = "${originalProject.name} (نسخة)",
            createdAt = now,
            updatedAt = now
        )
        val newProjectId = projectDao.insertProject(newProject)

        val sectors = sectorDao.getSectorsForProjectDirect(projectId)
        for (sector in sectors) {
            val newSector = SectorEntity(
                projectId = newProjectId,
                name = sector.name,
                createdAt = now
            )
            val newSectorId = sectorDao.insertSector(newSector)

            val sectorItems = sectorItemDao.getSectorItemsDirect(sector.id)
            val newSectorItems = sectorItems.map { item ->
                SectorItemEntity(
                    sectorId = newSectorId,
                    materialId = item.materialId,
                    quantity = item.quantity
                )
            }
            if (newSectorItems.isNotEmpty()) {
                sectorItemDao.insertSectorItems(newSectorItems)
            }
        }
        newProjectId
    }

    override fun getSectorSummaries(projectId: Long): Flow<List<SectorSummary>> {
        return sectorDao.getSectorSummariesForProject(projectId).map { list ->
            list.map { entity ->
                SectorSummary(
                    id = entity.id,
                    projectId = entity.projectId,
                    name = entity.name,
                    createdAt = entity.createdAt,
                    itemCount = entity.itemCount,
                    totalQuantity = entity.totalQuantity
                )
            }
        }
    }

    override fun getSector(sectorId: Long): Flow<Sector?> {
        return sectorDao.getSectorById(sectorId).map { entity ->
            entity?.let {
                Sector(
                    id = it.id,
                    projectId = it.projectId,
                    name = it.name,
                    createdAt = it.createdAt
                )
            }
        }
    }

    override suspend fun addSector(projectId: Long, name: String): Long = withContext(ioDispatcher) {
        val sector = SectorEntity(
            projectId = projectId,
            name = name.trim(),
            createdAt = System.currentTimeMillis()
        )
        val sectorId = sectorDao.insertSector(sector)
        // Update project updatedAt
        val project = projectDao.getProjectByIdDirect(projectId)
        if (project != null) {
            projectDao.updateProject(project.copy(updatedAt = System.currentTimeMillis()))
        }
        sectorId
    }

    override suspend fun updateSector(sectorId: Long, name: String) = withContext(ioDispatcher) {
        val existing = sectorDao.getSectorByIdDirect(sectorId)
        if (existing != null) {
            sectorDao.updateSector(existing.copy(name = name.trim()))
        }
    }

    override suspend fun deleteSector(sectorId: Long) = withContext(ioDispatcher) {
        sectorDao.deleteSectorById(sectorId)
    }

    override fun getAllMaterials(): Flow<List<MaterialItem>> {
        return materialDao.getAllMaterials().map { list ->
            list.map { entity ->
                MaterialItem(
                    id = entity.id,
                    name = entity.name,
                    unit = entity.unit,
                    isDefault = entity.isDefault
                )
            }
        }
    }

    override fun searchMaterials(query: String): Flow<List<MaterialItem>> {
        return materialDao.searchMaterials(query).map { list ->
            list.map { entity ->
                MaterialItem(
                    id = entity.id,
                    name = entity.name,
                    unit = entity.unit,
                    isDefault = entity.isDefault
                )
            }
        }
    }

    override fun getSectorSupplies(sectorId: Long): Flow<List<SectorSupplyItem>> {
        return sectorItemDao.getSectorItemsWithMaterial(sectorId).map { list ->
            list.map { item ->
                SectorSupplyItem(
                    materialId = item.sectorItem.materialId,
                    materialName = item.material.name,
                    unit = item.material.unit,
                    quantity = item.sectorItem.quantity
                )
            }
        }
    }

    override suspend fun updateSectorItemQuantity(
        sectorId: Long,
        materialId: Long,
        quantity: Int
    ) = withContext(ioDispatcher) {
        if (quantity <= 0) {
            // Rule: "المادة ذات الكمية = 0 لا تُحفظ"
            sectorItemDao.deleteSectorItemByKeys(sectorId, materialId)
        } else {
            sectorItemDao.upsertSectorItem(
                SectorItemEntity(
                    sectorId = sectorId,
                    materialId = materialId,
                    quantity = quantity
                )
            )
        }
        // Update project updatedAt timestamp
        val sector = sectorDao.getSectorByIdDirect(sectorId)
        if (sector != null) {
            val project = projectDao.getProjectByIdDirect(sector.projectId)
            if (project != null) {
                projectDao.updateProject(project.copy(updatedAt = System.currentTimeMillis()))
            }
        }
    }

    override fun getProjectInventory(projectId: Long): Flow<ProjectInventory?> {
        val projectFlow = getProject(projectId)
        val totalsFlow = sectorItemDao.getProjectMaterialTotals(projectId).map { list ->
            list.map {
                MaterialTotal(
                    materialId = it.materialId,
                    materialName = it.materialName,
                    unit = it.unit,
                    totalQuantity = it.totalQuantity
                )
            }
        }
        val detailsFlow = sectorItemDao.getProjectSectorMaterialDetails(projectId)

        return combine(projectFlow, totalsFlow, detailsFlow) { project, totals, details ->
            if (project == null) return@combine null

            val breakdowns = details.groupBy { it.sectorId }.map { (secId, items) ->
                val secName = items.firstOrNull()?.sectorName ?: "قطاع $secId"
                SectorBreakdown(
                    sectorId = secId,
                    sectorName = secName,
                    items = items.map {
                        SectorSupplyItem(
                            materialId = it.materialId,
                            materialName = it.materialName,
                            unit = it.unit,
                            quantity = it.quantity
                        )
                    }
                )
            }

            val grandTotalItems = totals.size
            val grandTotalQuantity = totals.sumOf { it.totalQuantity }

            ProjectInventory(
                project = project,
                totals = totals,
                sectorBreakdowns = breakdowns,
                grandTotalItems = grandTotalItems,
                grandTotalQuantity = grandTotalQuantity
            )
        }
    }

    override suspend fun exportBackupJson(): String = withContext(ioDispatcher) {
        val root = JSONObject()
        val projectsArray = JSONArray()
        val projects = projectDao.getAllProjectsDirect()

        for (p in projects) {
            val pObj = JSONObject()
            pObj.put("id", p.id)
            pObj.put("name", p.name)
            pObj.put("createdAt", p.createdAt)
            pObj.put("updatedAt", p.updatedAt)

            val sectorsArray = JSONArray()
            val sectors = sectorDao.getSectorsForProjectDirect(p.id)
            for (s in sectors) {
                val sObj = JSONObject()
                sObj.put("id", s.id)
                sObj.put("name", s.name)
                sObj.put("createdAt", s.createdAt)

                val itemsArray = JSONArray()
                val items = sectorItemDao.getSectorItemsDirect(s.id)
                for (it in items) {
                    val itObj = JSONObject()
                    itObj.put("materialId", it.materialId)
                    itObj.put("quantity", it.quantity)
                    itemsArray.put(itObj)
                }
                sObj.put("items", itemsArray)
                sectorsArray.put(sObj)
            }
            pObj.put("sectors", sectorsArray)
            projectsArray.put(pObj)
        }
        root.put("version", 1)
        root.put("exportedAt", System.currentTimeMillis())
        root.put("projects", projectsArray)
        root.toString(2)
    }

    override suspend fun importBackupJson(jsonString: String): Boolean = withContext(ioDispatcher) {
        try {
            val root = JSONObject(jsonString)
            val projectsArray = root.getJSONArray("projects")
            for (i in 0 until projectsArray.length()) {
                val pObj = projectsArray.getJSONObject(i)
                val pName = pObj.getString("name")
                val pCreated = pObj.optLong("createdAt", System.currentTimeMillis())
                val pUpdated = pObj.optLong("updatedAt", System.currentTimeMillis())
                val newProjectId = projectDao.insertProject(
                    ProjectEntity(name = pName, createdAt = pCreated, updatedAt = pUpdated)
                )

                if (pObj.has("sectors")) {
                    val sectorsArray = pObj.getJSONArray("sectors")
                    for (j in 0 until sectorsArray.length()) {
                        val sObj = sectorsArray.getJSONObject(j)
                        val sName = sObj.getString("name")
                        val sCreated = sObj.optLong("createdAt", System.currentTimeMillis())
                        val newSectorId = sectorDao.insertSector(
                            SectorEntity(projectId = newProjectId, name = sName, createdAt = sCreated)
                        )

                        if (sObj.has("items")) {
                            val itemsArray = sObj.getJSONArray("items")
                            for (k in 0 until itemsArray.length()) {
                                val itObj = itemsArray.getJSONObject(k)
                                val matId = itObj.getLong("materialId")
                                val qty = itObj.getInt("quantity")
                                if (qty > 0) {
                                    sectorItemDao.upsertSectorItem(
                                        SectorItemEntity(
                                            sectorId = newSectorId,
                                            materialId = matId,
                                            quantity = qty
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
