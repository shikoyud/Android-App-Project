package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import javax.inject.Inject

/**
 * Use case to update note and emoji for a selfie
 */
class UpdateNoteAndEmojiUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    suspend operator fun invoke(
        selfieId: Long,
        note: String,
        emoji: String?
    ): Result<Unit> {
        return try {
            repository.updateNoteAndEmoji(selfieId, note, emoji)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

