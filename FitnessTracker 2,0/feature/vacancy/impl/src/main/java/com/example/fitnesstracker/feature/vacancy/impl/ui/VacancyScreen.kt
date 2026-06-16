package com.example.fitnesstracker.feature.vacancy.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyScreen(viewModel: VacancyViewModel = hiltViewModel()) {
    val vacancy by viewModel.vacancy.collectAsState()
    val skills by viewModel.skills.collectAsState()
    val state by viewModel.state.collectAsState()
    val canAnalyze by viewModel.canAnalyze.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Анализ вакансии (ИИ)") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = vacancy,
                onValueChange = viewModel::onVacancyChange,
                label = { Text("Текст вакансии") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = skills,
                onValueChange = viewModel::onSkillsChange,
                label = { Text("Мои навыки") },
                placeholder = { Text("Kotlin, Android SDK, Retrofit, 1 год опыта…") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = viewModel::analyze,
                    enabled = canAnalyze,
                    modifier = Modifier.weight(1f),
                ) { Text("Проанализировать") }
                OutlinedButton(
                    onClick = viewModel::clear,
                    modifier = Modifier.weight(1f),
                ) { Text("Очистить") }
            }

            when (val s = state) {
                VacancyUiState.Idle -> Unit
                VacancyUiState.Loading -> Row(
                    Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator()
                    Text("  Анализируем…")
                }
                is VacancyUiState.Success -> Card(Modifier.fillMaxWidth()) {
                    Text(s.text, Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium)
                }
                is VacancyUiState.Error -> Text(
                    s.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
