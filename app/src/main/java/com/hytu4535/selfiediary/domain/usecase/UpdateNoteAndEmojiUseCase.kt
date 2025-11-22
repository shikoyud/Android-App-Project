package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import javax.inject.Inject

/**
 * Use case for updating note and emoji of a selfie
 */
class UpdateNoteAndEmojiUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    /**
     * Update note and emoji for a selfie
     */
    suspend fun execute(
        selfieId: Long,
        note: String,
        emoji: String?,
        tags: List<String> = emptyList()
    ): Result<Unit> {
        return try {
            repository.updateNoteAndEmoji(selfieId, note, emoji, tags)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update only note
     */
    suspend fun updateNote(selfieId: Long, note: String): Result<Unit> {
        return try {
            val selfie = repository.getSelfieById(selfieId)
            if (selfie != null) {
                repository.updateNoteAndEmoji(selfieId, note, selfie.emoji, selfie.tags)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Selfie not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update only emoji
     */
    suspend fun updateEmoji(selfieId: Long, emoji: String?): Result<Unit> {
        return try {
            val selfie = repository.getSelfieById(selfieId)
            if (selfie != null) {
                repository.updateNoteAndEmoji(selfieId, selfie.note, emoji, selfie.tags)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Selfie not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update only tags
     */
    suspend fun updateTags(selfieId: Long, tags: List<String>): Result<Unit> {
        return try {
            val selfie = repository.getSelfieById(selfieId)
            if (selfie != null) {
                repository.updateNoteAndEmoji(selfieId, selfie.note, selfie.emoji, tags)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Selfie not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

