package com.example.fitnesstracker.feature.workouts.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.feature.workouts.api.domain.model.WorkoutType
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.AddWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorkoutViewModel @Inject constructor(
    private val addWorkout: AddWorkoutUseCase,
) : ViewModel() {
    fun save(type: WorkoutType, durationMinutes: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            runCatching { addWorkout(type, durationMinutes) }
            onDone()
        }
    }
}
