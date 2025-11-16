package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import javax.inject.Inject

/**
 * Use case to check if user has taken a selfie today
 * Used by Smart Reminder system
 */
class HasSelfieTodayUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    suspend operator fun invoke(): Boolean {
        return try {
            repository.hasSelfieToday()
        } catch (e: Exception) {
            false
        }
    }
}

