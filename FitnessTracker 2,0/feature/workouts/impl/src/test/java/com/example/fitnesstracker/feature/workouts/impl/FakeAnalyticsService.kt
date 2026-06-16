package com.example.fitnesstracker.feature.workouts.impl

import com.example.fitnesstracker.core.common.AnalyticsService

/**
 * Тестовая заглушка фасада аналитики. Вместо обращения к AppMetrica просто
 * запоминает события — это позволяет проверять ViewModel в unit-тестах без
 * единого реального SDK (требование Лаб. №6).
 */
class FakeAnalyticsService : AnalyticsService {
    data class Event(val name: String, val params: Map<String, Any>)

    val events = mutableListOf<Event>()
    val errors = mutableListOf<String>()

    override fun trackEvent(name: String, params: Map<String, Any>) {
        events += Event(name, params)
    }
    override fun trackError(message: String, error: Throwable?) {
        errors += message
    }

    fun names(): List<String> = events.map { it.name }
}
