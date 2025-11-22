package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.local.dao.EmojiCount
import com.hytu4535.selfiediary.data.local.dao.MonthCount
import com.hytu4535.selfiediary.data.repository.SelfieRepository
import javax.inject.Inject

/**
 * Statistics data class
 */
data class SelfieStatistics(
    val totalCount: Int = 0,
    val editedCount: Int = 0,
    val syncedCount: Int = 0,
    val monthlyCount: Map<Int, Int> = emptyMap(), // Month (1-12) -> Count
    val mostUsedEmojis: List<EmojiCount> = emptyList(),
    val currentMonthCount: Int = 0,
    val lastMonthCount: Int = 0,
    val currentYearCount: Int = 0
)

/**
 * Use case for getting selfie statistics
 */
class GetStatisticsUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    /**
     * Get comprehensive selfie statistics
     */
    suspend fun execute(): SelfieStatistics {
        val totalCount = repository.getTotalCount()
        val editedCount = repository.getEditedCount()
        val syncedCount = repository.getSyncedCount()
        val monthCounts = repository.getCountByAllMonths()
        val mostUsedEmojis = repository.getMostUsedEmojis(limit = 10)

        // Convert list to map for easier access
        val monthlyCountMap = monthCounts.associate { it.monthOfYear to it.count }

        // Get current month info
        val currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
        val currentMonthCount = monthlyCountMap[currentMonth] ?: 0

        // Get last month info
        val lastMonth = if (currentMonth == 1) 12 else currentMonth - 1
        val lastMonthCount = monthlyCountMap[lastMonth] ?: 0

        // Calculate current year count
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val yearStart = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, currentYear)
            set(java.util.Calendar.MONTH, 0)
            set(java.util.Calendar.DAY_OF_MONTH, 1)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis

        val yearEnd = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, currentYear)
            set(java.util.Calendar.MONTH, 11)
            set(java.util.Calendar.DAY_OF_MONTH, 31)
            set(java.util.Calendar.HOUR_OF_DAY, 23)
            set(java.util.Calendar.MINUTE, 59)
            set(java.util.Calendar.SECOND, 59)
            set(java.util.Calendar.MILLISECOND, 999)
        }.timeInMillis

        val currentYearCount = repository.getCountByDateRange(yearStart, yearEnd)

        return SelfieStatistics(
            totalCount = totalCount,
            editedCount = editedCount,
            syncedCount = syncedCount,
            monthlyCount = monthlyCountMap,
            mostUsedEmojis = mostUsedEmojis,
            currentMonthCount = currentMonthCount,
            lastMonthCount = lastMonthCount,
            currentYearCount = currentYearCount
        )
    }

    /**
     * Get count for a specific month and year
     */
    suspend fun getCountByMonth(month: Int, year: Int): Int {
        return repository.getCountByMonth(month, year)
    }

    /**
     * Get most used emojis with optional limit
     */
    suspend fun getMostUsedEmojis(limit: Int = 10): List<EmojiCount> {
        return repository.getMostUsedEmojis(limit)
    }

    /**
     * Get monthly counts for all months
     */
    suspend fun getMonthlyBreakdown(): Map<Int, Int> {
        val monthCounts = repository.getCountByAllMonths()
        return monthCounts.associate { it.monthOfYear to it.count }
    }
}

