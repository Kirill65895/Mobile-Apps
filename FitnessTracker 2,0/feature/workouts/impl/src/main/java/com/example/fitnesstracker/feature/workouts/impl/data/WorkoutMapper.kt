package com.example.fitnesstracker.feature.workouts.impl.data

import com.example.fitnesstracker.core.database.WorkoutEntity
import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import com.example.fitnesstracker.feature.workouts.api.domain.model.WorkoutType

/** Маппинг между Room-сущностью (data) и доменной моделью. */
internal fun WorkoutEntity.toDomain(): Workout = Workout(
    id = id,
    type = runCatching { WorkoutType.valueOf(type) }.getOrDefault(WorkoutType.WALKING),
    durationMinutes = durationMinutes,
    caloriesBurned = caloriesBurned,
    timestamp = timestamp,
)

internal fun Workout.toEntity(): WorkoutEntity = WorkoutEntity(
    id = id,
    type = type.name,
    durationMinutes = durationMinutes,
    caloriesBurned = caloriesBurned,
    timestamp = timestamp,
)
