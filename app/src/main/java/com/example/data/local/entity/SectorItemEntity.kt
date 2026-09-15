package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing an item and quantity assigned to a specific sector.
 */
@Entity(
    tableName = "sector_items",
    foreignKeys = [
        ForeignKey(
            entity = SectorEntity::class,
            parentColumns = ["id"],
            childColumns = ["sectorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MaterialEntity::class,
            parentColumns = ["id"],
            childColumns = ["materialId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sectorId", "materialId"], unique = true),
        Index(value = ["sectorId"]),
        Index(value = ["materialId"])
    ]
)
data class SectorItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sectorId: Long,
    val materialId: Long,
    val quantity: Int
)
