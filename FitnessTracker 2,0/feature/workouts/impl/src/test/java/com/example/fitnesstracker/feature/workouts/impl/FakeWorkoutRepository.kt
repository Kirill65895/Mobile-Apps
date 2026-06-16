package com.example.fitnesstracker.feature.workouts.impl

import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import com.example.fitnesstracker.feature.workouts.api.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeWorkoutRepository : WorkoutRepository {
    private val state = MutableStateFlow<List<Workout>>(emptyList())
    override fun observeWorkouts() = state.asStateFlow()
    override suspend fun addWorkout(workout: Workout) { state.value = state.value + workout }
    override suspend fun deleteWorkout(workout: Workout) { state.value = state.value - workout }
}
