package com.hytu4535.selfiediary.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selfies")
data class SelfieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // File paths
    val filePath: String,
    val editedFilePath: String? = null,

    // Timestamp
    val timestamp: Long,

    // User content
    val note: String = "",
    val emoji: String? = null,
    val tags: List<String> = emptyList(),

    // Metadata
    val isEdited: Boolean = false,
    val isSynced: Boolean = false,

    // Statistics
    val dayOfMonth: Int = 0, // Day of month (1-31) for "On This Day"
    val monthOfYear: Int = 0  // Month of year (1-12)
)

