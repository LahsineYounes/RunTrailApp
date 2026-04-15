package com.example.runtrail.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.runtrail.data.db.entity.RunEntity
import com.example.runtrail.data.db.relation.RunWithLocations
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    // Returns a Flow so the UI auto-updates when new runs are inserted
    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getAllRuns(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs WHERE id = :runId")
    suspend fun getRunById(runId: String): RunEntity?

    // Transaction ensures both the run and its locations are fetched atomically
    @Transaction
    @Query("SELECT * FROM runs WHERE id = :runId")
    suspend fun getRunWithLocations(runId: String): RunWithLocations?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunEntity)

    @Delete
    suspend fun deleteRun(run: RunEntity)
}