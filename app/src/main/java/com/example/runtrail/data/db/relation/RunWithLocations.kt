package com.example.runtrail.data.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.runtrail.data.db.entity.LocationPointEntity
import com.example.runtrail.data.db.entity.RunEntity
import com.example.runtrail.data.db.entity.toDomainModel
import com.example.runtrail.domain.model.Run

// Room's @Relation automatically populates the locationPoints list
// when you query using RunDao.getRunWithLocations()
data class RunWithLocations(
    @Embedded val run: RunEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "runId"
    )
    val locationPoints: List<LocationPointEntity>
)

fun RunWithLocations.toDomainModel(): Run = run.toDomainModel().copy(
    locationPoints = locationPoints.map { it.toDomainModel() }
)