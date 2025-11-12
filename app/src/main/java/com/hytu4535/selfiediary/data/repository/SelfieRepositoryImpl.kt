package com.hytu4535.selfiediary.data.repository

import com.hytu4535.selfiediary.data.local.dao.SelfieDao
import com.hytu4535.selfiediary.data.local.entities.SelfieEntity
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelfieRepositoryImpl @Inject constructor(
    private val dao: SelfieDao
) : SelfieRepository {

    override fun getAllSelfies(): Flow<List<SelfieEntry>> {
        return dao.getAllSelfies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSelfieById(id: Long): SelfieEntry? {
        return dao.getSelfieById(id)?.toDomain()
    }

    override suspend fun insertSelfie(entry: SelfieEntry): Long {
        return dao.insertSelfie(entry.toEntity())
    }

    override suspend fun updateSelfie(entry: SelfieEntry) {
        dao.updateSelfie(entry.toEntity())
    }

    override suspend fun deleteSelfie(id: Long) {
        dao.deleteSelfieById(id)
    }

    private fun SelfieEntity.toDomain() = SelfieEntry(
        id = id,
        filePath = filePath,
        timestamp = timestamp,
        note = note
    )

    private fun SelfieEntry.toEntity() = SelfieEntity(
        id = id,
        filePath = filePath,
        timestamp = timestamp,
        note = note
    )
}

