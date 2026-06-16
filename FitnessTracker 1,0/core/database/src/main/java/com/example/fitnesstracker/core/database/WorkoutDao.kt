package com.example.fitnesstracker.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<WorkoutEntity>>

    @Insert
    suspend fun insert(entity: WorkoutEntity)

    @Delete
    suspend fun delete(entity: WorkoutEntity)
}
