package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import javax.inject.Inject

class DeleteSelfiesUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    /**
     * Delete multiple selfies in a batch operation
     * More efficient than deleting one by one
     */
    suspend operator fun invoke(ids: List<Long>): Result<Unit> {
        return try {
            if (ids.isEmpty()) {
                return Result.success(Unit)
            }
            repository.deleteSelfies(ids)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

