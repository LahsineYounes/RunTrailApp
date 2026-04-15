package com.example.runtrail.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.example.runtrail.domain.model.LocationPoint
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FusedLocationDataSource @Inject constructor(
    private val context: Context
) : LocationDataSource {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(intervalMs: Long): Flow<LocationPoint> =
        callbackFlow {
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                intervalMs
            ).apply {
                setMinUpdateIntervalMillis(intervalMs / 2)
                // Discard points with accuracy worse than 20 meters
                // — first line of defense against GPS drift
                setMinUpdateDistanceMeters(0f)
            }.build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        // Only emit if accuracy is acceptable
                        if (location.accuracy <= 20f) {
                            trySend(
                                LocationPoint(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    timestamp = location.time,
                                    accuracy = location.accuracy
                                )
                            )
                        }
                    }
                }
            }

            fusedClient.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            )

            // Called when the Flow collector is cancelled (e.g., service stops)
            awaitClose { fusedClient.removeLocationUpdates(callback) }
        }
}