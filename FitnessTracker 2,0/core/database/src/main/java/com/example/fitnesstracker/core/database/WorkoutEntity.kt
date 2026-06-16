package com.example.fitnesstracker.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Room-сущность. Это деталь реализации слоя data, живёт в :core:database. */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val timestamp: Long,
)
