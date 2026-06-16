package com.example.fitnesstracker.feature.statistics.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitnesstracker.core.ui.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
    val stats by viewModel.stats.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Статистика") }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatCard("Всего тренировок", stats.totalWorkouts.toString(), Modifier.fillMaxWidth())
            StatCard("Всего минут", stats.totalMinutes.toString(), Modifier.fillMaxWidth())
            StatCard("Сожжено ккал", stats.totalCalories.toString(), Modifier.fillMaxWidth())
            StatCard("Средняя длительность", "${stats.averageMinutes} мин", Modifier.fillMaxWidth())
        }
    }
}
