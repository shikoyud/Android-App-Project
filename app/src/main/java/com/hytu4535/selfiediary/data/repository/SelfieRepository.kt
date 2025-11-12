package com.hytu4535.selfiediary.data.repository

import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.flow.Flow

interface SelfieRepository {
    fun getAllSelfies(): Flow<List<SelfieEntry>>
    suspend fun getSelfieById(id: Long): SelfieEntry?
    suspend fun insertSelfie(entry: SelfieEntry): Long
    suspend fun updateSelfie(entry: SelfieEntry)
    suspend fun deleteSelfie(id: Long)
}

