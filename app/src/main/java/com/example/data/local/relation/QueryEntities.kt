package com.example.data.local.relation

import androidx.room.Embedded
import com.example.data.local.entity.MaterialEntity
import com.example.data.local.entity.SectorItemEntity

/**
 * Data class combining a SectorItem with its corresponding Material metadata.
 */
data class SectorItemWithMaterial(
    @Embedded val sectorItem: SectorItemEntity,
    @Embedded(prefix = "mat_") val material: MaterialEntity
)

/**
 * Data class for project list overview with computed statistics.
 */
data class ProjectSummaryEntity(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val sectorCount: Int,
    val totalItemsCount: Int,
    val totalQuantity: Int
)

/**
 * Data class for sector list overview with computed statistics.
 */
data class SectorSummaryEntity(
    val id: Long,
    val projectId: Long,
    val name: String,
    val createdAt: Long,
    val itemCount: Int,
    val totalQuantity: Int
)

/**
 * Aggregated material totals across an entire project.
 */
data class ProjectMaterialTotalEntity(
    val materialId: Long,
    val materialName: String,
    val unit: String,
    val totalQuantity: Int
)

/**
 * Material item per sector for detailed invoice breakdown.
 */
data class SectorMaterialDetailEntity(
    val sectorId: Long,
    val sectorName: String,
    val materialId: Long,
    val materialName: String,
    val unit: String,
    val quantity: Int
)
