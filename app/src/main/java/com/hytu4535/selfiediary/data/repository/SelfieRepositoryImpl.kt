package com.hytu4535.selfiediary.data.repository

import com.hytu4535.selfiediary.data.local.dao.SelfieDao
import com.hytu4535.selfiediary.data.local.entities.SelfieEntity
import com.hytu4535.selfiediary.domain.model.OnThisDayEntry
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class SelfieRepositoryImpl @Inject constructor(
    private val dao: SelfieDao
) : SelfieRepository {

    // ==================== BASIC CRUD ====================

    override fun getAllSelfies(): Flow<List<SelfieEntry>> {
        return dao.getAllSelfies()
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                // Log error and emit empty list
                println("Error getting all selfies: ${e.message}")
                emit(emptyList())
            }
    }

    override suspend fun getSelfieById(id: Long): SelfieEntry? {
        return try {
            dao.getSelfieById(id)?.toDomain()
        } catch (e: Exception) {
            println("Error getting selfie by id: ${e.message}")
            null
        }
    }

    override suspend fun insertSelfie(entry: SelfieEntry): Long {
        return try {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = entry.timestamp
            }
            val entityWithDates = entry.toEntity().copy(
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                monthOfYear = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based
            )
            dao.insertSelfie(entityWithDates)
        } catch (e: Exception) {
            println("Error inserting selfie: ${e.message}")
            -1L
        }
    }

    override suspend fun updateSelfie(entry: SelfieEntry) {
        try {
            dao.updateSelfie(entry.toEntity())
        } catch (e: Exception) {
            println("Error updating selfie: ${e.message}")
        }
    }

    override suspend fun deleteSelfie(id: Long) {
        try {
            dao.deleteSelfieById(id)
        } catch (e: Exception) {
            println("Error deleting selfie: ${e.message}")
        }
    }

    override suspend fun deleteSelfies(ids: List<Long>) {
        try {
            dao.deleteSelfiesByIds(ids)
        } catch (e: Exception) {
            println("Error deleting multiple selfies: ${e.message}")
        }
    }

    // ==================== DATE-BASED QUERIES ====================

    override fun getSelfiesToday(): Flow<List<SelfieEntry>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return dao.getSelfiesToday(startOfDay, endOfDay)
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                println("Error getting selfies today: ${e.message}")
                emit(emptyList())
            }
    }

    override fun getSelfiesByDateRange(
        startTimestamp: Long,
        endTimestamp: Long
    ): Flow<List<SelfieEntry>> {
        return dao.getSelfiesByDateRange(startTimestamp, endTimestamp)
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                println("Error getting selfies by date range: ${e.message}")
                emit(emptyList())
            }
    }

    override fun getSelfiesByMonth(month: Int, year: Int): Flow<List<SelfieEntry>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, 0, 1, 0, 0, 0)
        val yearStart = calendar.timeInMillis

        calendar.set(year, 11, 31, 23, 59, 59)
        val yearEnd = calendar.timeInMillis

        return dao.getSelfiesByMonth(month, yearStart, yearEnd)
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                println("Error getting selfies by month: ${e.message}")
                emit(emptyList())
            }
    }

    override suspend fun hasSelfieToday(): Boolean {
        return try {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            val endOfDay = calendar.timeInMillis

            dao.hasSelfieToday(startOfDay, endOfDay) > 0
        } catch (e: Exception) {
            println("Error checking if has selfie today: ${e.message}")
            false
        }
    }

    // ==================== ON THIS DAY ====================

    override fun getOnThisDaySelfies(): Flow<List<OnThisDayEntry>> {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // 0-based to 1-based

        // Get start of current year
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val currentYearStart = calendar.timeInMillis

        return dao.getOnThisDaySelfies(currentDay, currentMonth, currentYearStart)
            .map { entities ->
                entities.map { entity ->
                    val entryCalendar = Calendar.getInstance().apply {
                        timeInMillis = entity.timestamp
                    }
                    val yearsAgo = currentYear - entryCalendar.get(Calendar.YEAR)

                    OnThisDayEntry(
                        selfieEntry = entity.toDomain(),
                        yearsAgo = yearsAgo
                    )
                }
            }
            .catch { e ->
                println("Error getting on this day selfies: ${e.message}")
                emit(emptyList())
            }
    }

    // ==================== SEARCH ====================

    override fun searchByNote(query: String): Flow<List<SelfieEntry>> {
        return dao.searchByNote(query)
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                println("Error searching by note: ${e.message}")
                emit(emptyList())
            }
    }

    override fun searchByEmoji(emoji: String): Flow<List<SelfieEntry>> {
        return dao.searchByEmoji(emoji)
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                println("Error searching by emoji: ${e.message}")
                emit(emptyList())
            }
    }

    override fun searchByTag(tag: String): Flow<List<SelfieEntry>> {
        return dao.searchByTag(tag)
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                println("Error searching by tag: ${e.message}")
                emit(emptyList())
            }
    }

    override fun searchAll(query: String): Flow<List<SelfieEntry>> {
        return dao.searchAll(query)
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e ->
                println("Error searching all: ${e.message}")
                emit(emptyList())
            }
    }

    // ==================== STATISTICS ====================

    override suspend fun getTotalCount(): Int {
        return try {
            dao.getTotalCount()
        } catch (e: Exception) {
            println("Error getting total count: ${e.message}")
            0
        }
    }

    override suspend fun getEditedCount(): Int {
        return try {
            dao.getEditedCount()
        } catch (e: Exception) {
            println("Error getting edited count: ${e.message}")
            0
        }
    }

    override suspend fun getSyncedCount(): Int {
        return try {
            dao.getSyncedCount()
        } catch (e: Exception) {
            println("Error getting synced count: ${e.message}")
            0
        }
    }

    override suspend fun getCountByMonth(month: Int, year: Int): Int {
        return try {
            val calendar = Calendar.getInstance()
            calendar.set(year, 0, 1, 0, 0, 0)
            val yearStart = calendar.timeInMillis

            calendar.set(year, 11, 31, 23, 59, 59)
            val yearEnd = calendar.timeInMillis

            dao.getCountByMonth(month, yearStart, yearEnd)
        } catch (e: Exception) {
            println("Error getting count by month: ${e.message}")
            0
        }
    }

    override suspend fun getCountByDateRange(startTimestamp: Long, endTimestamp: Long): Int {
        return try {
            // Use first() to get only one emission and avoid infinite waiting
            val entities = dao.getSelfiesByDateRange(startTimestamp, endTimestamp).first()
            entities.size
        } catch (e: Exception) {
            println("Error getting count by date range: ${e.message}")
            0
        }
    }

    override suspend fun getMostUsedEmojis(limit: Int): List<com.hytu4535.selfiediary.data.local.dao.EmojiCount> {
        return try {
            dao.getMostUsedEmojis(limit)
        } catch (e: Exception) {
            println("Error getting most used emojis: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getCountByAllMonths(): List<com.hytu4535.selfiediary.data.local.dao.MonthCount> {
        return try {
            dao.getCountByAllMonths()
        } catch (e: Exception) {
            println("Error getting count by all months: ${e.message}")
            emptyList()
        }
    }

    // ==================== NOTE & EMOJI ====================

    override suspend fun updateNoteAndEmoji(id: Long, note: String, emoji: String?, tags: List<String>) {
        try {
            val selfie = dao.getSelfieById(id)
            if (selfie != null) {
                val updated = selfie.copy(note = note, emoji = emoji, tags = tags)
                dao.updateSelfie(updated)
            }
        } catch (e: Exception) {
            println("Error updating note and emoji: ${e.message}")
        }
    }

    // ==================== MAPPERS ====================

    private fun SelfieEntity.toDomain() = SelfieEntry(
        id = id,
        filePath = filePath,
        editedFilePath = editedFilePath,
        timestamp = timestamp,
        note = note,
        emoji = emoji,
        tags = tags,
        isEdited = isEdited,
        isSynced = isSynced,
        dayOfMonth = dayOfMonth,
        monthOfYear = monthOfYear
    )

    private fun SelfieEntry.toEntity() = SelfieEntity(
        id = id,
        filePath = filePath,
        editedFilePath = editedFilePath,
        timestamp = timestamp,
        note = note,
        emoji = emoji,
        tags = tags,
        isEdited = isEdited,
        isSynced = isSynced,
        dayOfMonth = dayOfMonth,
        monthOfYear = monthOfYear
    )
}

