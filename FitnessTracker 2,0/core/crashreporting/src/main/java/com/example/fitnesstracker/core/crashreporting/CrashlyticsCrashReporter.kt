package com.example.fitnesstracker.core.crashreporting

import com.example.fitnesstracker.core.common.CrashReporter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

/**
 * Реализация фасада поверх Firebase Crashlytics. Единственное место с типом
 * FirebaseCrashlytics. Все вызовы обёрнуты в runCatching — отсутствие реального
 * google-services.json не должно ронять приложение.
 */
internal class CrashlyticsCrashReporter @Inject constructor() : CrashReporter {

    private val crashlytics get() = FirebaseCrashlytics.getInstance()

    override fun log(message: String) {
        runCatching { crashlytics.log(message) }
    }

    override fun recordException(throwable: Throwable, context: Map<String, String>) {
        runCatching {
            context.forEach { (k, v) -> crashlytics.setCustomKey(k, v) }
            crashlytics.recordException(throwable)
        }
    }

    override fun setUserId(id: String) {
        runCatching { crashlytics.setUserId(id) }
    }

    override fun setCustomKey(key: String, value: String) {
        runCatching { crashlytics.setCustomKey(key, value) }
    }
}
