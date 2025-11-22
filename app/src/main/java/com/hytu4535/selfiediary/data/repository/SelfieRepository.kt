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

    fun searchByNote(query: String): Flow<List<SelfieEntry>>

    fun searchByEmoji(emoji: String): Flow<List<SelfieEntry>>

    fun searchByTag(tag: String): Flow<List<SelfieEntry>>

    fun searchAll(query: String): Flow<List<SelfieEntry>>

    // ==================== STATISTICS ====================

    suspend fun getTotalCount(): Int

    suspend fun getEditedCount(): Int

    suspend fun getSyncedCount(): Int

    suspend fun getCountByMonth(month: Int, year: Int): Int

    suspend fun getCountByDateRange(startTimestamp: Long, endTimestamp: Long): Int

    suspend fun getMostUsedEmojis(limit: Int): List<com.hytu4535.selfiediary.data.local.dao.EmojiCount>

    suspend fun getCountByAllMonths(): List<com.hytu4535.selfiediary.data.local.dao.MonthCount>

    // ==================== NOTE & EMOJI ====================

    suspend fun updateNoteAndEmoji(id: Long, note: String, emoji: String?, tags: List<String> = emptyList())
}

