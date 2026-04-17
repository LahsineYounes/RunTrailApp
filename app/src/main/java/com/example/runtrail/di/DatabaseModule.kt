package com.example.runtrail.di

import android.content.Context
import androidx.room.Room
import com.example.runtrail.data.db.RunTrailDatabase
import com.example.runtrail.data.db.dao.LocationPointDao
import com.example.runtrail.data.db.dao.RunDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // One DB instance for the whole app lifetime
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RunTrailDatabase =
        Room.databaseBuilder(
            context,
            RunTrailDatabase::class.java,
            "runtrail.db"
        ).build()

    @Provides
    fun provideRunDao(db: RunTrailDatabase): RunDao = db.runDao()

    @Provides
    fun provideLocationPointDao(db: RunTrailDatabase): LocationPointDao =
        db.locationPointDao()
}