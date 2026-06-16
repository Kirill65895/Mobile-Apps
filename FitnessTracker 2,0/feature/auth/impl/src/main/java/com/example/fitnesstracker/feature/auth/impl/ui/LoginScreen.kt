package com.example.fitnesstracker.feature.auth.impl.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val emailValid by viewModel.emailValid.collectAsState()
    val activity = LocalContext.current.findActivity()

    LaunchedEffect(state) {
        if (state is LoginUiState.LoggedIn) onLoggedIn()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Fitness Tracker", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Войдите или зарегистрируйтесь",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 28.dp),
        )

        if (state is LoginUiState.Loading) {
            CircularProgressIndicator()
        } else {
            // --- Регистрация по Google-почте с проверкой адреса ---
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Google-почта (gmail.com)") },
                singleLine = true,
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = { activity?.let(viewModel::loginWithGoogle) },
                enabled = emailValid,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            ) { Text("Продолжить с Google") }

            HorizontalDivider(Modifier.padding(vertical = 20.dp))

            Button(
                onClick = { activity?.let(viewModel::loginWithVk) },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Войти через ВКонтакте") }

            OutlinedButton(
                onClick = { activity?.let(viewModel::loginWithYandex) },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            ) { Text("Войти через Яндекс") }
        }

        (state as? LoginUiState.Failed)?.let {
            Text(
                it.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

/** Достаёт Activity из Compose-контекста (нужно для вызова SDK авторизации). */
private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
