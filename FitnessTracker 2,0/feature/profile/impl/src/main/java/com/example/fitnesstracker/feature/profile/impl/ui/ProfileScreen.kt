package com.example.fitnesstracker.feature.profile.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.pm.ApplicationInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLoggedOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    // Надёжная замена BuildConfig.DEBUG (в library-модуле он всегда false):
    // показываем диагностику только если приложение собрано как отлаживаемое (debug).
    val showDebugTools = remember(context) {
        (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
    LaunchedEffect(Unit) { viewModel.onScreenViewed() }

    Scaffold(topBar = { TopAppBar(title = { Text("Профиль") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Баннер из Remote Config (меняется удалённо без перезапуска приложения).
            if (state.bannerText.isNotBlank()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(state.bannerText, Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium)
                }
            }

            // Данные профиля из Firestore (обновляются в реальном времени).
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Данные пользователя", style = MaterialTheme.typography.titleMedium)
                    val p = state.profile
                    if (p == null) {
                        Text("Профиль не загружен", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        InfoRow("Имя", p.name)
                        InfoRow("E-mail", p.email ?: "—")
                        InfoRow("FCM-токен", p.fcmToken?.take(24)?.plus("…") ?: "ещё не получен")
                    }
                }
            }

            // Экспериментальная функция, управляемая флагом Remote Config.
            if (state.experimentEnabled) {
                Card(Modifier.fillMaxWidth()) {
                    Text(
                        "🧪 Экспериментальная функция включена удалённо (Remote Config)",
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            // Учебная диагностика крашлитики (только в debug-сборке).
            if (showDebugTools) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Диагностика (только debug)", style = MaterialTheme.typography.titleSmall)
                        TextButton(onClick = { viewModel.triggerNonFatal() }) {
                            Text("Отправить тестовую non-fatal ошибку")
                        }
                        TextButton(onClick = { viewModel.triggerTestCrash() }) {
                            Text("Вызвать тестовый краш", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Смена профиля / выход.
            OutlinedButton(
                onClick = { viewModel.logout(); onLoggedOut() },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Сменить профиль") }

            Button(
                onClick = { viewModel.logout(); onLoggedOut() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Выйти из профиля") }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    androidx.compose.foundation.layout.Row(Modifier.fillMaxWidth()) {
        Text("$label: ", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
