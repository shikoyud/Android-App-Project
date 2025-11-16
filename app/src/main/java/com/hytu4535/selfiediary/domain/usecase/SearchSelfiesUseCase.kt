package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to search selfies by query
 * Searches in note, emoji, and tags
 */
class SearchSelfiesUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    /**
     * Search all fields (note, emoji, tags)
     */
    operator fun invoke(query: String): Flow<List<SelfieEntry>> {
        return if (query.isBlank()) {
            repository.getAllSelfies()
        } else {
            repository.searchSelfies(query.trim())
        }
    }

    /**
     * Search by specific emoji
     */
    fun searchByEmoji(emoji: String): Flow<List<SelfieEntry>> {
        return repository.searchByEmoji(emoji)
    }

    /**
     * Search by specific tag
     */
    fun searchByTag(tag: String): Flow<List<SelfieEntry>> {
        return repository.searchByTag(tag)
    }
}

