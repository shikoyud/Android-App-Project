package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get selfies by date range
 */
class GetSelfiesByDateRangeUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    operator fun invoke(startTimestamp: Long, endTimestamp: Long): Flow<List<SelfieEntry>> {
        return repository.getSelfiesByDateRange(startTimestamp, endTimestamp)
    }
}

