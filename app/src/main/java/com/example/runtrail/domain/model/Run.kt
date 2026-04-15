package com.example.runtrail.domain.model

import com.example.runtrail.data.db.entity.RunEntity

data class Run(
    val id: String,
    val timestamp: Long,
    val durationInMillis: Long,
    val totalDistanceMeters: Int,
    val averagePaceMinPerKm: Float,
    val locationPoints: List<LocationPoint> = emptyList(),
    val mapSnapshotUri: String? = null
)

fun Run.toEntity() = RunEntity(
    id = id, timestamp = timestamp,
    durationInMillis = durationInMillis,
    totalDistanceMeters = totalDistanceMeters,
    averagePaceMinPerKm = averagePaceMinPerKm,
    mapSnapshotUri = mapSnapshotUri
)