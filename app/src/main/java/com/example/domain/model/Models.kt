package com.example.domain.model

/**
 * Domain model for Project.
 */
data class Project(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Domain model for Sector / Room.
 */
data class Sector(
    val id: Long = 0,
    val projectId: Long,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Domain model for Material / Supply item.
 */
data class MaterialItem(
    val id: Long = 0,
    val name: String,
    val unit: String = "قطعة",
    val isDefault: Boolean = true
)

/**
 * Domain model for a material with its assigned quantity in a sector.
 */
data class SectorSupplyItem(
    val materialId: Long,
    val materialName: String,
    val unit: String,
    val quantity: Int
)

/**
 * Domain model for Project summary item on the home screen.
 */
data class ProjectSummary(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val sectorCount: Int,
    val totalItemsCount: Int,
    val totalQuantity: Int
)

/**
 * Domain model for Sector summary on the project detail screen.
 */
data class SectorSummary(
    val id: Long,
    val projectId: Long,
    val name: String,
    val createdAt: Long,
    val itemCount: Int,
    val totalQuantity: Int
)

/**
 * Aggregated total for an item across the entire project.
 */
data class MaterialTotal(
    val materialId: Long,
    val materialName: String,
    val unit: String,
    val totalQuantity: Int
)

/**
 * Breakdown of materials by sector for detailed inventory view.
 */
data class SectorBreakdown(
    val sectorId: Long,
    val sectorName: String,
    val items: List<SectorSupplyItem>
)

/**
 * Complete inventory summary for a project.
 */
data class ProjectInventory(
    val project: Project,
    val totals: List<MaterialTotal>,
    val sectorBreakdowns: List<SectorBreakdown>,
    val grandTotalItems: Int,
    val grandTotalQuantity: Int
)
