package com.example.runtrail.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runtrail.data.db.dao.LocationPointDao
import com.example.runtrail.data.db.dao.RunDao
import com.example.runtrail.data.db.entity.LocationPointEntity
import com.example.runtrail.data.db.entity.RunEntity

@Database(
    entities = [RunEntity::class, LocationPointEntity::class],
    version = 1,
    exportSchema = true  // Good practice: export schema for migration diffs
)
abstract class RunTrailDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
    abstract fun locationPointDao(): LocationPointDao
}