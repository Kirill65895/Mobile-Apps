package com.example.fitnesstracker.feature.workouts.api.domain.usecase

import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import com.example.fitnesstracker.feature.workouts.api.domain.repository.WorkoutRepository
import javax.inject.Inject

class DeleteWorkoutUseCase @Inject constructor(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(workout: Workout) = repository.deleteWorkout(workout)
}
