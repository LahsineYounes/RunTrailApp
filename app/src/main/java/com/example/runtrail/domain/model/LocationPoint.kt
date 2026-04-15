package com.example.runtrail.domain.model

import com.example.runtrail.data.db.entity.LocationPointEntity

data class LocationPoint(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val accuracy: Float = 0f  // Used for Kalman filter / accuracy thresholding
)

fun LocationPoint.toEntity(runId: String) = LocationPointEntity(
    runId = runId, latitude = latitude,
    longitude = longitude, timestamp = timestamp,
    accuracy = accuracy
)