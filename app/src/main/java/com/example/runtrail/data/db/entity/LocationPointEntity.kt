package com.example.runtrail.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo
import com.example.runtrail.domain.model.LocationPoint
import androidx.room.Table as TableInfo




@Entity(
    tableName = "location_points",
    foreignKeys = [
        ForeignKey(
            entity = RunEntity::class,
            parentColumns = ["id"],
            childColumns = ["runId"],
            // CASCADE ensures location points are deleted when a run is deleted
            onDelete = TableInfo.ForeignKey.CASCADE
        )
    ],
    indices = [Index("runId")]  // Index for fast join queries
)
data class LocationPointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val runId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val accuracy: Float
)

fun LocationPointEntity.toDomainModel() = LocationPoint(
    latitude = latitude, longitude = longitude,
    timestamp = timestamp, accuracy = accuracy
)