package com.example.fitnesstracker.feature.workouts.api.domain.usecase

import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import com.example.fitnesstracker.feature.workouts.api.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** UseCase живёт в domain-слое и оперирует только абстракциями. */
class GetWorkoutsUseCase @Inject constructor(
    private val repository: WorkoutRepository,
) {
    operator fun invoke(): Flow<List<Workout>> = repository.observeWorkouts()
}
