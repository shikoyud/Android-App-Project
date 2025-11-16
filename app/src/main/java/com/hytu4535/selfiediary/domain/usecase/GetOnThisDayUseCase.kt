package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.OnThisDayEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnThisDayUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    /**
     * Get selfies taken on this day in previous years
     * Returns a Flow that emits the list of OnThisDayEntry
     * Sorted by most recent year first
     */
    operator fun invoke(): Flow<List<OnThisDayEntry>> {
        return repository.getOnThisDaySelfies()
    }

    /**
     * Get only the most recent selfie from this day in previous years
     */
    fun getMostRecent(): Flow<OnThisDayEntry?> {
        return repository.getOnThisDaySelfies()
            .map { list -> list.firstOrNull() }
    }
}

