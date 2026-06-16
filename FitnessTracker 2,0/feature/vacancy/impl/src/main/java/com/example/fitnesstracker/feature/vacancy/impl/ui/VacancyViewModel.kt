package com.example.fitnesstracker.feature.vacancy.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.feature.vacancy.api.domain.model.AnalysisResult
import com.example.fitnesstracker.feature.vacancy.api.domain.repository.VacancyAnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface VacancyUiState {
    data object Idle : VacancyUiState
    data object Loading : VacancyUiState
    data class Success(val text: String) : VacancyUiState
    data class Error(val message: String) : VacancyUiState
}

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val repository: VacancyAnalysisRepository,
) : ViewModel() {

    private val _vacancy = MutableStateFlow("")
    val vacancy: StateFlow<String> = _vacancy.asStateFlow()

    private val _skills = MutableStateFlow("")
    val skills: StateFlow<String> = _skills.asStateFlow()

    private val _state = MutableStateFlow<VacancyUiState>(VacancyUiState.Idle)
    val state: StateFlow<VacancyUiState> = _state.asStateFlow()

    // Кнопка «Проанализировать» активна только при заполненных обоих полях.
    val canAnalyze: StateFlow<Boolean> =
        combine(_vacancy, _skills, _state) { v, s, st ->
            v.isNotBlank() && s.isNotBlank() && st != VacancyUiState.Loading
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun onVacancyChange(value: String) { _vacancy.value = value }
    fun onSkillsChange(value: String) { _skills.value = value }

    fun analyze() {
        if (_vacancy.value.isBlank() || _skills.value.isBlank()) return
        _state.value = VacancyUiState.Loading
        viewModelScope.launch {
            _state.value = when (val r = repository.analyze(_vacancy.value, _skills.value)) {
                is AnalysisResult.Success -> VacancyUiState.Success(r.text)
                is AnalysisResult.Error -> VacancyUiState.Error(r.message)
            }
        }
    }

    fun clear() {
        _vacancy.value = ""
        _skills.value = ""
        _state.value = VacancyUiState.Idle
    }
}
