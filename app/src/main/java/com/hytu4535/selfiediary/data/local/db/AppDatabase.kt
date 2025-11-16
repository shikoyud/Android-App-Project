package com.hytu4535.selfiediary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hytu4535.selfiediary.data.local.dao.SelfieDao
import com.hytu4535.selfiediary.data.local.entities.SelfieEntity

/**
 * TypeConverter for List<String> to String and vice versa
 */
class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split(",").filter { it.isNotEmpty() }
    }
}

@Database(
    entities = [SelfieEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selfieDao(): SelfieDao

    companion object {
        /**
         * Migration from version 1 to version 2
         * Adds new columns: emoji, tags, isEdited, editedFilePath, isSynced, dayOfMonth, monthOfYear
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add new columns with default values
                db.execSQL(
                    "ALTER TABLE selfies ADD COLUMN emoji TEXT"
                )
                db.execSQL(
                    "ALTER TABLE selfies ADD COLUMN tags TEXT NOT NULL DEFAULT ''"
                )
                db.execSQL(
                    "ALTER TABLE selfies ADD COLUMN isEdited INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE selfies ADD COLUMN editedFilePath TEXT"
                )
                db.execSQL(
                    "ALTER TABLE selfies ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE selfies ADD COLUMN dayOfMonth INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE selfies ADD COLUMN monthOfYear INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}


