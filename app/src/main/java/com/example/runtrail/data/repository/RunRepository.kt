package com.example.runtrail.data.repository

import com.example.runtrail.domain.model.LocationPoint
import com.example.runtrail.domain.model.Run
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    fun getAllRuns(): Flow<List<Run>>
    suspend fun getRunWithLocations(runId: String): Run?
    suspend fun saveRun(run: Run, locationPoints: List<LocationPoint>)
    suspend fun deleteRun(runId: String)
}