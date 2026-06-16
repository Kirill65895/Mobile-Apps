package com.example.fitnesstracker.feature.workouts.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitnesstracker.feature.workouts.api.domain.model.WorkoutType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    onSaved: () -> Unit,
    viewModel: AddWorkoutViewModel = hiltViewModel(),
) {
    var selectedType by remember { mutableStateOf(WorkoutType.RUNNING) }
    var durationText by remember { mutableStateOf("30") }

    Scaffold(topBar = { TopAppBar(title = { Text("Новая тренировка") }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Тип тренировки")
            WorkoutType.entries.forEach { type ->
                Row(type, selectedType == type) { selectedType = type }
            }
            OutlinedTextField(
                value = durationText,
                onValueChange = { durationText = it.filter(Char::isDigit) },
                label = { Text("Длительность (мин)") },
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = {
                    val minutes = durationText.toIntOrNull() ?: 0
                    viewModel.save(selectedType, minutes, onSaved)
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Сохранить") }
        }
    }
}

@Composable
private fun Row(type: WorkoutType, selected: Boolean, onClick: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth().selectable(selected = selected, onClick = onClick),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(type.displayName)
    }
}
