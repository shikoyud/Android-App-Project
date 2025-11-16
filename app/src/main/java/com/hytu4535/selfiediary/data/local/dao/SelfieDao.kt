package com.hytu4535.selfiediary.data.local.dao

import androidx.room.*
import com.hytu4535.selfiediary.data.local.entities.SelfieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelfieDao {

    // ==================== BASIC CRUD OPERATIONS ====================

    @Query("SELECT * FROM selfies ORDER BY timestamp DESC")
    fun getAllSelfies(): Flow<List<SelfieEntity>>

    @Query("SELECT * FROM selfies WHERE id = :id")
    suspend fun getSelfieById(id: Long): SelfieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelfie(selfie: SelfieEntity): Long

    @Update
    suspend fun updateSelfie(selfie: SelfieEntity)

    @Delete
    suspend fun deleteSelfie(selfie: SelfieEntity)

    @Query("DELETE FROM selfies WHERE id = :id")
    suspend fun deleteSelfieById(id: Long)

    @Query("DELETE FROM selfies WHERE id IN (:ids)")
    suspend fun deleteSelfiesByIds(ids: List<Long>)

    // ==================== DATE-BASED QUERIES ====================

    /**
     * Get selfies taken today (from midnight to now)
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE timestamp >= :startOfDay AND timestamp <= :endOfDay
        ORDER BY timestamp DESC
    """)
    fun getSelfiesToday(startOfDay: Long, endOfDay: Long): Flow<List<SelfieEntity>>

    /**
     * Get selfies within a date range
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE timestamp >= :startTimestamp AND timestamp <= :endTimestamp
        ORDER BY timestamp DESC
    """)
    fun getSelfiesByDateRange(startTimestamp: Long, endTimestamp: Long): Flow<List<SelfieEntity>>

    /**
     * Get selfies for a specific month and year
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE monthOfYear = :month AND timestamp >= :yearStart AND timestamp <= :yearEnd
        ORDER BY timestamp DESC
    """)
    fun getSelfiesByMonth(month: Int, yearStart: Long, yearEnd: Long): Flow<List<SelfieEntity>>

    // ==================== ON THIS DAY FEATURE ====================

    /**
     * Get selfies taken on the same day and month in previous years
     * Excludes current year
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE dayOfMonth = :day 
        AND monthOfYear = :month 
        AND timestamp < :currentYearStart
        ORDER BY timestamp DESC
    """)
    fun getOnThisDaySelfies(day: Int, month: Int, currentYearStart: Long): Flow<List<SelfieEntity>>

    /**
     * Check if user has taken a selfie today
     */
    @Query("""
        SELECT COUNT(*) FROM selfies 
        WHERE timestamp >= :startOfDay AND timestamp <= :endOfDay
    """)
    suspend fun hasSelfieToday(startOfDay: Long, endOfDay: Long): Int

    // ==================== SEARCH QUERIES ====================

    /**
     * Search selfies by note content
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE note LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchByNote(query: String): Flow<List<SelfieEntity>>

    /**
     * Search selfies by emoji
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE emoji = :emoji
        ORDER BY timestamp DESC
    """)
    fun searchByEmoji(emoji: String): Flow<List<SelfieEntity>>

    /**
     * Search selfies by tags (approximate match)
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE tags LIKE '%' || :tag || '%'
        ORDER BY timestamp DESC
    """)
    fun searchByTag(tag: String): Flow<List<SelfieEntity>>

    /**
     * Combined search: note, emoji, or tags
     */
    @Query("""
        SELECT * FROM selfies 
        WHERE note LIKE '%' || :query || '%'
        OR emoji LIKE '%' || :query || '%'
        OR tags LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchAll(query: String): Flow<List<SelfieEntity>>

    // ==================== STATISTICS QUERIES ====================

    /**
     * Count total selfies
     */
    @Query("SELECT COUNT(*) FROM selfies")
    suspend fun getTotalCount(): Int

    /**
     * Count selfies by month
     */
    @Query("""
        SELECT COUNT(*) FROM selfies 
        WHERE monthOfYear = :month AND timestamp >= :yearStart AND timestamp <= :yearEnd
    """)
    suspend fun getCountByMonth(month: Int, yearStart: Long, yearEnd: Long): Int

    /**
     * Get most used emoji with count
     */
    @Query("""
        SELECT emoji, COUNT(*) as count FROM selfies 
        WHERE emoji IS NOT NULL AND emoji != ''
        GROUP BY emoji 
        ORDER BY count DESC 
        LIMIT :limit
    """)
    suspend fun getMostUsedEmojis(limit: Int = 10): List<EmojiCount>

    /**
     * Get selfies grouped by month (for statistics)
     */
    @Query("""
        SELECT monthOfYear, COUNT(*) as count FROM selfies 
        GROUP BY monthOfYear 
        ORDER BY monthOfYear ASC
    """)
    suspend fun getCountByAllMonths(): List<MonthCount>

    /**
     * Get edited selfies count
     */
    @Query("SELECT COUNT(*) FROM selfies WHERE isEdited = 1")
    suspend fun getEditedCount(): Int

    /**
     * Get synced selfies count
     */
    @Query("SELECT COUNT(*) FROM selfies WHERE isSynced = 1")
    suspend fun getSyncedCount(): Int
}

/**
 * Data class for emoji statistics
 */
data class EmojiCount(
    val emoji: String,
    val count: Int
)

/**
 * Data class for month statistics
 */
data class MonthCount(
    val monthOfYear: Int,
    val count: Int
)


