package com.example.fitnesstracker.feature.auth.impl.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.feature.auth.api.AuthResult
import com.example.fitnesstracker.feature.auth.api.AuthService
import com.example.fitnesstracker.feature.auth.api.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class LoggedIn(val user: User) : LoginUiState
    data class Failed(val message: String) : LoginUiState
}

/** ViewModel зависит ТОЛЬКО от интерфейса AuthService (требование Лаб. №6). */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun loginWithVk(activity: Activity) = run { authService.loginWithVk(activity) }
    fun loginWithYandex(activity: Activity) = run { authService.loginWithYandex(activity) }

    private fun run(block: suspend () -> AuthResult) {
        viewModelScope.launch {
            _state.value = LoginUiState.Loading
            _state.value = when (val result = block()) {
                is AuthResult.Success -> LoginUiState.LoggedIn(result.user)
                is AuthResult.Error -> LoginUiState.Failed(result.message)
                AuthResult.Cancelled -> LoginUiState.Idle
            }
        }
    }
}
