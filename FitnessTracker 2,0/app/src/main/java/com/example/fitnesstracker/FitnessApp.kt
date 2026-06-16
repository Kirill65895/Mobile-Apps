package com.example.fitnesstracker

import android.app.Application
import com.example.fitnesstracker.core.analytics.AnalyticsInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FitnessApp : Application() {

    @Inject lateinit var analyticsInitializer: AnalyticsInitializer

    override fun onCreate() {
        super.onCreate()
        // Инициализация SDK строго в Application (Лаб. №6, «Ошибка 1»).
        analyticsInitializer.init(this)
    }
}
