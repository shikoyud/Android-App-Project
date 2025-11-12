package com.hytu4535.selfiediary.di

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.data.repository.SelfieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSelfieRepository(
        impl: SelfieRepositoryImpl
    ): SelfieRepository
}

