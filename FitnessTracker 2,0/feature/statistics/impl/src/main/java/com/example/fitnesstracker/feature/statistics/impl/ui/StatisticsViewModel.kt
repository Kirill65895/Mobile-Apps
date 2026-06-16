package com.example.fitnesstracker.feature.statistics.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.feature.statistics.api.domain.model.WorkoutStatistics
import com.example.fitnesstracker.feature.statistics.api.domain.usecase.GetWorkoutStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getStatistics: GetWorkoutStatisticsUseCase,
) : ViewModel() {
    val stats: StateFlow<WorkoutStatistics> = getStatistics()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            WorkoutStatistics(0, 0, 0, 0),
        )
}
