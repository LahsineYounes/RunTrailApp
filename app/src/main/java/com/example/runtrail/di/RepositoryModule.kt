package com.example.runtrail.di

import com.example.runtrail.data.repository.RunRepository
import com.example.runtrail.data.repository.RunRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRunRepository(
        impl: RunRepositoryImpl
    ): RunRepository
}