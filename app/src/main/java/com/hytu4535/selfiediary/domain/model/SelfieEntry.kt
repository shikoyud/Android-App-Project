package com.hytu4535.selfiediary.domain.model

data class SelfieEntry(
    val id: Long = 0,
    val filePath: String,
    val timestamp: Long,
    val note: String = ""
)

