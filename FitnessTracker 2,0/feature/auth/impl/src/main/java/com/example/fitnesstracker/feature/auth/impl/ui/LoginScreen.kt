package com.example.fitnesstracker.feature.auth.impl.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
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
            "Войдите, чтобы сохранять тренировки",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
        )

        if (state is LoginUiState.Loading) {
            CircularProgressIndicator()
        } else {
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
