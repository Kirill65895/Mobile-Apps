package com.example.fitnesstracker.feature.workouts.api.domain.model

/** Доменная модель тренировки. Чистый Kotlin, без Android и без Room. */
data class Workout(
    val id: Long,
    val type: WorkoutType,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val timestamp: Long,
)
