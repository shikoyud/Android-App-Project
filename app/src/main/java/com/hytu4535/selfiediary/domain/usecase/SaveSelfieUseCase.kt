package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import javax.inject.Inject

class SaveSelfieUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    suspend operator fun invoke(entry: SelfieEntry): Result<Long> {
        return try {
            val id = repository.insertSelfie(entry)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

