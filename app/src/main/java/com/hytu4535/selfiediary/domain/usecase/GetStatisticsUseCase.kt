package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.SelfieStatistics
import javax.inject.Inject

/**
 * Use case to get selfie statistics
 */
class GetStatisticsUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    suspend operator fun invoke(): Result<SelfieStatistics> {
        return try {
            val stats = repository.getStatistics()
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

