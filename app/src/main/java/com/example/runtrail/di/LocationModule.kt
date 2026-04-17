package com.example.runtrail.di

import com.example.runtrail.data.location.FusedLocationDataSource
import com.example.runtrail.data.location.LocationDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    // Hilt binds the interface to its implementation via abstract function
    @Binds
    @Singleton
    abstract fun bindLocationDataSource(
        impl: FusedLocationDataSource
    ): LocationDataSource
}