package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.SectorEntity
import com.example.data.local.relation.SectorSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SectorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSector(sector: SectorEntity): Long

    @Update
    suspend fun updateSector(sector: SectorEntity)

    @Delete
    suspend fun deleteSector(sector: SectorEntity)

    @Query("DELETE FROM sectors WHERE id = :id")
    suspend fun deleteSectorById(id: Long)

    @Query("SELECT * FROM sectors WHERE projectId = :projectId ORDER BY createdAt ASC")
    fun getSectorsForProject(projectId: Long): Flow<List<SectorEntity>>

    @Query("""
        SELECT 
            s.id,
            s.projectId,
            s.name,
            s.createdAt,
            COUNT(DISTINCT si.id) AS itemCount,
            COALESCE(SUM(si.quantity), 0) AS totalQuantity
        FROM sectors s
        LEFT JOIN sector_items si ON si.sectorId = s.id AND si.quantity > 0
        WHERE s.projectId = :projectId
        GROUP BY s.id
        ORDER BY s.createdAt ASC
    """)
    fun getSectorSummariesForProject(projectId: Long): Flow<List<SectorSummaryEntity>>

    @Query("SELECT * FROM sectors WHERE id = :sectorId LIMIT 1")
    fun getSectorById(sectorId: Long): Flow<SectorEntity?>

    @Query("SELECT * FROM sectors WHERE id = :sectorId LIMIT 1")
    suspend fun getSectorByIdDirect(sectorId: Long): SectorEntity?

    @Query("SELECT * FROM sectors WHERE projectId = :projectId")
    suspend fun getSectorsForProjectDirect(projectId: Long): List<SectorEntity>
}
