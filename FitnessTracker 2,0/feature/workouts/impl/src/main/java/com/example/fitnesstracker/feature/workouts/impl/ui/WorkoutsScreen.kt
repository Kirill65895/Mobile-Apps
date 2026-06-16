package com.example.fitnesstracker.feature.workouts.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutsScreen(
    onAddClick: () -> Unit,
    viewModel: WorkoutsViewModel = hiltViewModel(),
) {
    val workouts by viewModel.workouts.collectAsState()

    LaunchedEffect(Unit) { viewModel.onScreenViewed() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Мои тренировки") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                text = { Text("Добавить") },
                icon = { Text("+") },
            )
        },
    ) { padding ->
        if (workouts.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Пока нет тренировок. Нажмите «Добавить».")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(workouts, key = { it.id }) { workout ->
                    Card {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    workout.type.displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                TextButton(onClick = { viewModel.onDelete(workout) }) {
                                    Text("Удалить")
                                }
                            }
                            Text("${workout.durationMinutes} мин • ${workout.caloriesBurned} ккал")
                        }
                    }
                }
            }
        }
    }
}
