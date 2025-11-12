package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.OnThisDayEntry
import java.util.*
import javax.inject.Inject

class GetOnThisDayUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    suspend operator fun invoke(): OnThisDayEntry? {
        // TODO: Implement logic to get selfie from same date in previous years
        return null
    }
}

