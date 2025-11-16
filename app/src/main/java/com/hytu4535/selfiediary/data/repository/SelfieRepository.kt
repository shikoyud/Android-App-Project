package com.hytu4535.selfiediary.data.repository

import com.hytu4535.selfiediary.domain.model.OnThisDayEntry
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.domain.model.SelfieStatistics
import kotlinx.coroutines.flow.Flow

interface SelfieRepository {

    // ==================== BASIC CRUD ====================

    fun getAllSelfies(): Flow<List<SelfieEntry>>

    suspend fun getSelfieById(id: Long): SelfieEntry?

    suspend fun insertSelfie(entry: SelfieEntry): Long

    suspend fun updateSelfie(entry: SelfieEntry)

    suspend fun deleteSelfie(id: Long)

    suspend fun deleteSelfies(ids: List<Long>)

    // ==================== DATE-BASED QUERIES ====================

    fun getSelfiesToday(): Flow<List<SelfieEntry>>

    fun getSelfiesByDateRange(startTimestamp: Long, endTimestamp: Long): Flow<List<SelfieEntry>>

    fun getSelfiesByMonth(month: Int, year: Int): Flow<List<SelfieEntry>>

    suspend fun hasSelfieToday(): Boolean

    // ==================== ON THIS DAY ====================

    fun getOnThisDaySelfies(): Flow<List<OnThisDayEntry>>

    // ==================== SEARCH ====================

    fun searchSelfies(query: String): Flow<List<SelfieEntry>>

    fun searchByEmoji(emoji: String): Flow<List<SelfieEntry>>

    fun searchByTag(tag: String): Flow<List<SelfieEntry>>

    // ==================== STATISTICS ====================

    suspend fun getStatistics(): SelfieStatistics

    // ==================== NOTE & EMOJI ====================

    suspend fun updateNoteAndEmoji(id: Long, note: String, emoji: String?)
}

