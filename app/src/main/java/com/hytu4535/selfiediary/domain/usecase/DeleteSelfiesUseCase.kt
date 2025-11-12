package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import javax.inject.Inject

class DeleteSelfiesUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    suspend operator fun invoke(ids: List<Long>): Result<Unit> {
        return try {
            ids.forEach { id ->
                repository.deleteSelfie(id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

