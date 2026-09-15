package com.example.domain.repository

import com.example.domain.model.MaterialItem
import com.example.domain.model.Project
import com.example.domain.model.ProjectInventory
import com.example.domain.model.ProjectSummary
import com.example.domain.model.Sector
import com.example.domain.model.SectorSummary
import com.example.domain.model.SectorSupplyItem
import kotlinx.coroutines.flow.Flow

interface ElectricRepository {

    // Projects
    fun getProjectSummaries(): Flow<List<ProjectSummary>>
    fun searchProjectSummaries(query: String): Flow<List<ProjectSummary>>
    fun getProject(id: Long): Flow<Project?>
    suspend fun addProject(name: String): Long
    suspend fun updateProject(id: Long, name: String)
    suspend fun deleteProject(id: Long)
    suspend fun duplicateProject(projectId: Long): Long

    // Sectors
    fun getSectorSummaries(projectId: Long): Flow<List<SectorSummary>>
    fun getSector(sectorId: Long): Flow<Sector?>
    suspend fun addSector(projectId: Long, name: String): Long
    suspend fun updateSector(sectorId: Long, name: String)
    suspend fun deleteSector(sectorId: Long)

    // Materials & Supplies
    fun getAllMaterials(): Flow<List<MaterialItem>>
    fun searchMaterials(query: String): Flow<List<MaterialItem>>
    fun getSectorSupplies(sectorId: Long): Flow<List<SectorSupplyItem>>
    suspend fun updateSectorItemQuantity(sectorId: Long, materialId: Long, quantity: Int)

    // Inventory
    fun getProjectInventory(projectId: Long): Flow<ProjectInventory?>

    // Backup & Restore
    suspend fun exportBackupJson(): String
    suspend fun importBackupJson(jsonString: String): Boolean
}
