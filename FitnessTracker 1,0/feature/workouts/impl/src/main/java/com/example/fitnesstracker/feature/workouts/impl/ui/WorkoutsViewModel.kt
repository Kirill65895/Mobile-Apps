package com.example.fitnesstracker.feature.workouts.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.DeleteWorkoutUseCase
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.GetWorkoutsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    getWorkouts: GetWorkoutsUseCase,
    private val deleteWorkout: DeleteWorkoutUseCase,
) : ViewModel() {

    val workouts: StateFlow<List<Workout>> = getWorkouts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onDelete(workout: Workout) {
        viewModelScope.launch { deleteWorkout(workout) }
    }
}
