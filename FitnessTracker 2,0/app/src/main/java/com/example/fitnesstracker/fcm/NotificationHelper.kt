package com.example.fitnesstracker.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Создание канала и показ уведомлений. Канал нужен на API 26+ (иначе уведомление молча игнорируется). */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Основные уведомления", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Уведомления Fitness Tracker" }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)   // идемпотентно
        }
    }

    /** Показывает уведомление; по нажатию открывает экран [route], передавая data через extras. */
    fun show(title: String, body: String, route: String?, data: Map<String, String>) {
        ensureChannel()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_ROUTE, route)
            data.forEach { (k, v) -> putExtra(k, v) }
        }
        val pending = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notify)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pending)
            .build()
        runCatching {
            NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    companion object {
        const val CHANNEL_ID = "fitness_default"
        const val EXTRA_ROUTE = "open_route"
    }
}
