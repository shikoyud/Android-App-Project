package com.hytu4535.selfiediary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hytu4535.selfiediary.data.local.dao.SelfieDao
import com.hytu4535.selfiediary.data.local.entities.SelfieEntity

@Database(
    entities = [SelfieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selfieDao(): SelfieDao
}

