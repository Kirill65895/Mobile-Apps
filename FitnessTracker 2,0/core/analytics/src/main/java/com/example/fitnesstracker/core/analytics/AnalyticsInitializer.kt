package com.example.fitnesstracker.core.analytics

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Единая точка инициализации AppMetrica. Вызывается ОДИН раз из класса Application
 * (см. типичную «Ошибку 1» из методички — инициализация не в Application).
 *
 * Инициализация защищённая: если ключ не задан в local.properties, SDK не
 * активируется и приложение продолжает работать (события просто игнорируются).
 */
@Singleton
class AnalyticsInitializer @Inject constructor() {

    @Volatile
    var activated: Boolean = false
        private set

    fun init(application: Application) {
        val key = BuildConfig.APPMETRICA_API_KEY
        if (key.isBlank() || key == "null") {
            // Ключ не настроен — работаем без аналитики, не роняя приложение.
            return
        }
        val config = AppMetricaConfig.newConfigBuilder(key)
            .withLogs()                 // подробные логи в Logcat (только для отладки)
            .withSessionTimeout(60)     // сессия завершается через 60 сек неактивности
            .build()
        AppMetrica.activate(application.applicationContext, config)
        AppMetrica.enableActivityAutoTracking(application)
        activated = true
    }
}
