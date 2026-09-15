package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.SectorItemEntity
import com.example.data.local.relation.ProjectMaterialTotalEntity
import com.example.data.local.relation.SectorItemWithMaterial
import com.example.data.local.relation.SectorMaterialDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SectorItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSectorItem(item: SectorItemEntity): Long

    @Delete
    suspend fun deleteSectorItem(item: SectorItemEntity)

    @Query("DELETE FROM sector_items WHERE sectorId = :sectorId AND materialId = :materialId")
    suspend fun deleteSectorItemByKeys(sectorId: Long, materialId: Long)

    @Query("DELETE FROM sector_items WHERE sectorId = :sectorId AND quantity <= 0")
    suspend fun cleanupZeroQuantityItems(sectorId: Long)

    @Query("SELECT * FROM sector_items WHERE sectorId = :sectorId")
    fun getSectorItems(sectorId: Long): Flow<List<SectorItemEntity>>

    @Query("SELECT * FROM sector_items WHERE sectorId = :sectorId")
    suspend fun getSectorItemsDirect(sectorId: Long): List<SectorItemEntity>

    @Transaction
    @Query("""
        SELECT 
            si.id AS id,
            si.sectorId AS sectorId,
            si.materialId AS materialId,
            si.quantity AS quantity,
            m.id AS mat_id,
            m.name AS mat_name,
            m.unit AS mat_unit,
            m.isDefault AS mat_isDefault
        FROM sector_items si
        INNER JOIN materials m ON m.id = si.materialId
        WHERE si.sectorId = :sectorId AND si.quantity > 0
        ORDER BY m.id ASC
    """)
    fun getSectorItemsWithMaterial(sectorId: Long): Flow<List<SectorItemWithMaterial>>

    @Query("""
        SELECT 
            m.id AS materialId,
            m.name AS materialName,
            m.unit AS unit,
            SUM(si.quantity) AS totalQuantity
        FROM sector_items si
        INNER JOIN materials m ON m.id = si.materialId
        INNER JOIN sectors s ON s.id = si.sectorId
        WHERE s.projectId = :projectId AND si.quantity > 0
        GROUP BY m.id, m.name, m.unit
        ORDER BY totalQuantity DESC, m.name ASC
    """)
    fun getProjectMaterialTotals(projectId: Long): Flow<List<ProjectMaterialTotalEntity>>

    @Query("""
        SELECT 
            s.id AS sectorId,
            s.name AS sectorName,
            m.id AS materialId,
            m.name AS materialName,
            m.unit AS unit,
            si.quantity AS quantity
        FROM sector_items si
        INNER JOIN sectors s ON s.id = si.sectorId
        INNER JOIN materials m ON m.id = si.materialId
        WHERE s.projectId = :projectId AND si.quantity > 0
        ORDER BY s.id ASC, m.id ASC
    """)
    fun getProjectSectorMaterialDetails(projectId: Long): Flow<List<SectorMaterialDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSectorItems(items: List<SectorItemEntity>)

    @Query("SELECT * FROM sector_items")
    suspend fun getAllSectorItemsDirect(): List<SectorItemEntity>
}
