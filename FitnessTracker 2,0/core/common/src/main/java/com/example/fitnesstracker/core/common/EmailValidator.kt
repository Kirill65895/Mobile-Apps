package com.example.fitnesstracker.core.common

/** Результат проверки e-mail. */
sealed interface EmailValidationResult {
    data object Valid : EmailValidationResult
    data class Invalid(val message: String) : EmailValidationResult
}

/**
 * Проверка адреса Google-почты при регистрации (Лаб. №7+).
 * Лежит в :core:common (чистый Kotlin) — не зависит от Android, легко тестируется.
 */
object EmailValidator {

    // Базовая проверка формата e-mail (без android.util.Patterns, т.к. модуль чистый).
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // Домены Google-почты.
    private val GOOGLE_DOMAINS = setOf("gmail.com", "googlemail.com")

    fun validateGoogleEmail(raw: String): EmailValidationResult {
        val email = raw.trim()
        return when {
            email.isBlank() -> EmailValidationResult.Invalid("Введите адрес почты")
            !EMAIL_REGEX.matches(email) -> EmailValidationResult.Invalid("Некорректный формат e-mail")
            email.substringAfterLast('@').lowercase() !in GOOGLE_DOMAINS ->
                EmailValidationResult.Invalid("Нужен адрес Google (@gmail.com)")
            else -> EmailValidationResult.Valid
        }
    }

    fun isValidGoogleEmail(raw: String): Boolean =
        validateGoogleEmail(raw) is EmailValidationResult.Valid
}
