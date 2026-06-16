package com.example.fitnesstracker

import android.app.Application
import com.example.fitnesstracker.core.analytics.AnalyticsInitializer
import com.example.fitnesstracker.core.common.RemoteConfigService
import com.example.fitnesstracker.fcm.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FitnessApp : Application() {

    @Inject lateinit var analyticsInitializer: AnalyticsInitializer
    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var remoteConfigService: RemoteConfigService

    override fun onCreate() {
        super.onCreate()
        analyticsInitializer.init(this)        // Лаб. №6
        notificationHelper.ensureChannel()     // Лаб. №7: канал до первого уведомления
        remoteConfigService.start()            // Лаб. №7: загрузка конфигурации при старте
    }
}
