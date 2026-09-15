package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing an electrical supply or material.
 */
@Entity(
    tableName = "materials",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val unit: String = "قطعة",
    val isDefault: Boolean = true
)
