package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.MaterialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMaterials(materials: List<MaterialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: MaterialEntity): Long

    @Update
    suspend fun updateMaterial(material: MaterialEntity)

    @Query("SELECT * FROM materials ORDER BY id ASC")
    fun getAllMaterials(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materials WHERE name LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchMaterials(query: String): Flow<List<MaterialEntity>>

    @Query("SELECT COUNT(*) FROM materials")
    suspend fun countMaterials(): Int

    @Query("SELECT * FROM materials")
    suspend fun getAllMaterialsDirect(): List<MaterialEntity>
}
