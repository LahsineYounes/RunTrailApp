package com.example.runtrail.domain.usecase

import android.location.Location
import com.example.runtrail.data.location.LocationDataSource
import com.example.runtrail.domain.model.LocationPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Orchestrates location emissions into distance/pace calculations.
// This is the core business logic of the app.
class TrackRunUseCase @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val calculatePace: CalculatePaceUseCase
) {
    /**
     * Emits a stream of RunState.Running updates.
     * Each emission contains the current location point.
     */
    fun execute(startTimeMillis: Long): Flow<LocationPoint> =
        locationDataSource
            .getLocationUpdates(intervalMs = 1000L)
            .map { point -> point }  // Passthrough; distance calculated in VM

    /**
     * Calculates distance between two lat/lng points using the Haversine formula.
     * More accurate than simple Euclidean distance on a spherical surface.
     */
    fun calculateDistance(p1: LocationPoint, p2: LocationPoint): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            p1.latitude, p1.longitude,
            p2.latitude, p2.longitude,
            results
        )
        return results[0]  // Distance in meters
    }
}