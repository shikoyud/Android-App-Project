package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSelfiesUseCase @Inject constructor(
    private val repository: SelfieRepository
) {
    operator fun invoke(): Flow<List<SelfieEntry>> {
        return repository.getAllSelfies()
    }
}

