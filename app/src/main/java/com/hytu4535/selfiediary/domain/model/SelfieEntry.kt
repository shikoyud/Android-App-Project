package com.hytu4535.selfiediary.domain.model

data class SelfieEntry(
    val id: Long = 0,
    val filePath: String,
    val editedFilePath: String? = null,
    val timestamp: Long,
    val note: String = "",
    val emoji: String? = null,
    val tags: List<String> = emptyList(),
    val isEdited: Boolean = false,
    val isSynced: Boolean = false,
    val dayOfMonth: Int = 0,
    val monthOfYear: Int = 0
)

