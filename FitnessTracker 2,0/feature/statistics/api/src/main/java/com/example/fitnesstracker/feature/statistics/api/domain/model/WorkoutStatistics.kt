package com.example.fitnesstracker.feature.statistics.api.domain.model

data class WorkoutStatistics(
    val totalWorkouts: Int,
    val totalMinutes: Int,
    val totalCalories: Int,
    val averageMinutes: Int,
)
