package com.example.fitnesstracker.feature.auth.impl.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.core.common.EmailValidationResult
import com.example.fitnesstracker.core.common.EmailValidator
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

/** ViewModel зависит только от интерфейса AuthService и чистого валидатора почты. */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    // --- Регистрация по Google-почте ---
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _emailValid = MutableStateFlow(false)
    val emailValid: StateFlow<Boolean> = _emailValid.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
        when (val result = EmailValidator.validateGoogleEmail(value)) {
            EmailValidationResult.Valid -> {
                _emailValid.value = true
                _emailError.value = null
            }
            is EmailValidationResult.Invalid -> {
                _emailValid.value = false
                // Пока поле пустое — ошибку не показываем (не пугаем пользователя сразу).
                _emailError.value = if (value.isBlank()) null else result.message
            }
        }
    }

    fun loginWithGoogle(activity: Activity) {
        val result = EmailValidator.validateGoogleEmail(_email.value)
        if (result is EmailValidationResult.Invalid) {
            _emailError.value = result.message
            return
        }
        run { authService.loginWithGoogle(activity, _email.value.trim()) }
    }

    fun loginWithVk(activity: Activity) = run { authService.loginWithVk(activity) }
    fun loginWithYandex(activity: Activity) = run { authService.loginWithYandex(activity) }

    private fun run(block: suspend () -> AuthResult) {
        viewModelScope.launch {
            _state.value = LoginUiState.Loading
            _state.value = when (val r = block()) {
                is AuthResult.Success -> LoginUiState.LoggedIn(r.user)
                is AuthResult.Error -> LoginUiState.Failed(r.message)
                AuthResult.Cancelled -> LoginUiState.Idle
            }
        }
    }
}
