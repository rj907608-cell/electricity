package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.relation.ProjectSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    fun getProjectById(id: Long): Flow<ProjectEntity?>

    @Query("""
        SELECT 
            p.id,
            p.name,
            p.createdAt,
            p.updatedAt,
            COUNT(DISTINCT s.id) AS sectorCount,
            COUNT(DISTINCT si.id) AS totalItemsCount,
            COALESCE(SUM(si.quantity), 0) AS totalQuantity
        FROM projects p
        LEFT JOIN sectors s ON s.projectId = p.id
        LEFT JOIN sector_items si ON si.sectorId = s.id AND si.quantity > 0
        GROUP BY p.id
        ORDER BY p.updatedAt DESC
    """)
    fun getProjectSummaries(): Flow<List<ProjectSummaryEntity>>

    @Query("""
        SELECT 
            p.id,
            p.name,
            p.createdAt,
            p.updatedAt,
            COUNT(DISTINCT s.id) AS sectorCount,
            COUNT(DISTINCT si.id) AS totalItemsCount,
            COALESCE(SUM(si.quantity), 0) AS totalQuantity
        FROM projects p
        LEFT JOIN sectors s ON s.projectId = p.id
        LEFT JOIN sector_items si ON si.sectorId = s.id AND si.quantity > 0
        WHERE p.name LIKE '%' || :query || '%'
        GROUP BY p.id
        ORDER BY p.updatedAt DESC
    """)
    fun searchProjectSummaries(query: String): Flow<List<ProjectSummaryEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectByIdDirect(id: Long): ProjectEntity?

    @Query("SELECT * FROM projects")
    suspend fun getAllProjectsDirect(): List<ProjectEntity>
}
