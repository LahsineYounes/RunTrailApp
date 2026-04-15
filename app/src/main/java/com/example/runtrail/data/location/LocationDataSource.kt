package com.example.runtrail.data.location

import com.example.runtrail.domain.model.LocationPoint
import kotlinx.coroutines.flow.Flow

// Interface defined in data layer, implemented by FusedLocationDataSource.
// This abstraction lets us swap with a mock in tests.
interface LocationDataSource {
    fun getLocationUpdates(intervalMs: Long): Flow<LocationPoint>
}