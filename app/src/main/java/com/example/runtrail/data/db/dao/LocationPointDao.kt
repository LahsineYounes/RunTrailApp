package com.example.runtrail.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.runtrail.data.db.entity.LocationPointEntity

@Dao
interface LocationPointDao {

    // Batch insert — critical for performance during active runs
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(points: List<LocationPointEntity>)

    @Query("SELECT * FROM location_points WHERE runId = :runId ORDER BY timestamp ASC")
    suspend fun getPointsForRun(runId: String): List<LocationPointEntity>
}