package com.example.fitnesstracker.feature.about.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import android.view.LayoutInflater
import android.widget.RatingBar
import android.widget.TextView
import com.example.fitnesstracker.feature.about.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(viewModel: AboutViewModel = hiltViewModel()) {
    val office = viewModel.office
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.onScreenViewed() }

    Scaffold(topBar = { TopAppBar(title = { Text("О нас") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(office.companyName, style = MaterialTheme.typography.headlineSmall)
                    Text(office.description, style = MaterialTheme.typography.bodyMedium)
                    Text("Адрес офиса:", style = MaterialTheme.typography.labelLarge)
                    Text(office.address, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // XML↔Compose interop (Семестровая): классический RatingBar из XML внутри Compose.
            // Данные передаются в обе стороны: Compose-состояние -> View и обратно через слушатель.
            var rating by remember { mutableStateOf(0f) }
            Card(Modifier.fillMaxWidth()) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        LayoutInflater.from(ctx).inflate(R.layout.view_app_rating, null, false)
                    },
                    update = { view ->
                        val bar = view.findViewById<RatingBar>(R.id.ratingBar)
                        val label = view.findViewById<TextView>(R.id.ratingLabel)
                        bar.rating = rating
                        label.text =
                            if (rating > 0f) "Ваша оценка: ${rating.toInt()} из 5" else "Оцените приложение"
                        bar.setOnRatingBarChangeListener { _, value, fromUser ->
                            if (fromUser) rating = value
                        }
                    },
                )
            }

            OfficeMap(modifier = Modifier.fillMaxWidth().height(200.dp))

            Button(
                onClick = { viewModel.onBuildRoute(context) },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Построить маршрут до офиса") }
        }
    }
}

/**
 * Встроенная карта-заглушка (рисуется Canvas, без API-ключа). Здесь подключается
 * реальная интерактивная карта Google/Yandex — компонент меняется в одном месте.
 */
@Composable
private fun OfficeMap(modifier: Modifier = Modifier) {
    val land = Color(0xFFE6EFE6)
    val road = Color(0xFFBFD8BF)
    val pin = Color(0xFFD32F2F)
    Card(modifier = modifier) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                drawRect(land)
                val step = size.width / 6
                for (i in 1..5) {
                    drawLine(road, Offset(i * step, 0f), Offset(i * step, size.height), 6f)
                    drawLine(road, Offset(0f, i * (size.height / 5)), Offset(size.width, i * (size.height / 5)), 6f)
                }
                val c = Offset(size.width / 2, size.height / 2)
                drawCircle(pin, radius = 16f, center = c)
                drawCircle(Color.White, radius = 6f, center = c)
            }
            Text(
                "Офис «ФитТрек»",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 56.dp),
            )
        }
    }
}
