package com.hytu4535.selfiediary.data.local.dao

import androidx.room.*
import com.hytu4535.selfiediary.data.local.entities.SelfieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelfieDao {
    @Query("SELECT * FROM selfies ORDER BY timestamp DESC")
    fun getAllSelfies(): Flow<List<SelfieEntity>>

    @Query("SELECT * FROM selfies WHERE id = :id")
    suspend fun getSelfieById(id: Long): SelfieEntity?

    @Insert
    suspend fun insertSelfie(selfie: SelfieEntity): Long

    @Update
    suspend fun updateSelfie(selfie: SelfieEntity)

    @Delete
    suspend fun deleteSelfie(selfie: SelfieEntity)

    @Query("DELETE FROM selfies WHERE id = :id")
    suspend fun deleteSelfieById(id: Long)
}

