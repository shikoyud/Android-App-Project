package com.hytu4535.selfiediary.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selfies")
data class SelfieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val filePath: String,
    val timestamp: Long,
    val note: String = ""
)

