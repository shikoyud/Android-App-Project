package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching selfies by various criteria
 */
class SearchSelfiesUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    /**
     * Search selfies by note content
     */
    fun searchByNote(query: String): Flow<List<SelfieEntry>> {
        return repository.searchByNote(query)
    }

    /**
     * Search selfies by emoji
     */
    fun searchByEmoji(emoji: String): Flow<List<SelfieEntry>> {
        return repository.searchByEmoji(emoji)
    }

    /**
     * Search selfies by tag
     */
    fun searchByTag(tag: String): Flow<List<SelfieEntry>> {
        return repository.searchByTag(tag)
    }

    /**
     * Combined search across all fields
     */
    fun searchAll(query: String): Flow<List<SelfieEntry>> {
        return repository.searchAll(query)
    }

    /**
     * Search selfies by date range
     */
    fun searchByDateRange(startTimestamp: Long, endTimestamp: Long): Flow<List<SelfieEntry>> {
        return repository.getSelfiesByDateRange(startTimestamp, endTimestamp)
    }

    /**
     * Search selfies by specific month and year
     */
    fun searchByMonth(month: Int, year: Int): Flow<List<SelfieEntry>> {
        return repository.getSelfiesByMonth(month, year)
    }
}

