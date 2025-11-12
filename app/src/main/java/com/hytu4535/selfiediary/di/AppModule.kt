package com.hytu4535.selfiediary.di

import android.content.Context
import androidx.room.Room
import com.hytu4535.selfiediary.data.local.db.AppDatabase
import com.hytu4535.selfiediary.data.local.dao.SelfieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "selfie_diary_db"
        ).build()
    }

    @Provides
    fun provideSelfieDao(database: AppDatabase): SelfieDao {
        return database.selfieDao()
    }
}

