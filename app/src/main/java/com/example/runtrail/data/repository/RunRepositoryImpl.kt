package com.example.runtrail.data.repository

import com.example.runtrail.data.db.dao.LocationPointDao
import com.example.runtrail.data.db.dao.RunDao
import com.example.runtrail.data.db.entity.toDomainModel
import com.example.runtrail.data.db.relation.toDomainModel
import com.example.runtrail.domain.model.LocationPoint
import com.example.runtrail.domain.model.Run
import com.example.runtrail.domain.model.toEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RunRepositoryImpl @Inject constructor(
    private val runDao: RunDao,
    private val locationPointDao: LocationPointDao
) : RunRepository {

    override fun getAllRuns(): Flow<List<Run>> =
        runDao.getAllRuns().map { entities ->
            entities.map { it.toDomainModel() }
        }

    override suspend fun getRunWithLocations(runId: String): Run? =
        runDao.getRunWithLocations(runId)?.toDomainModel()

    override suspend fun saveRun(run: Run, locationPoints: List<LocationPoint>) {
        // Wrap in a transaction — both inserts succeed or both fail
        runDao.insertRun(run.toEntity())
        locationPointDao.insertAll(
            locationPoints.map { it.toEntity(run.id) }
        )
    }

    override suspend fun deleteRun(runId: String) {
        runDao.getRunById(runId)?.let { runDao.deleteRun(it) }
    }
}
