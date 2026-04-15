package com.example.runtrail.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runtrail.domain.model.Run

@Entity(tableName = "runs")
data class RunEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val durationInMillis: Long,
    val totalDistanceMeters: Int,
    val averagePaceMinPerKm: Float,
    val mapSnapshotUri: String?
)

fun RunEntity.toDomainModel() = Run(
    id = id, timestamp = timestamp,
    durationInMillis = durationInMillis,
    totalDistanceMeters = totalDistanceMeters,
    averagePaceMinPerKm = averagePaceMinPerKm,
    mapSnapshotUri = mapSnapshotUri
)