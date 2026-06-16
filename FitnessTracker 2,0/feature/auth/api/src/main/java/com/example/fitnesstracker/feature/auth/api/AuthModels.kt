package com.example.fitnesstracker.feature.auth.api

/** Собственная модель пользователя — НЕ из SDK (граница изоляции). */
data class User(
    val id: String,
    val name: String,
    val email: String?,
    val avatarUrl: String?,
    val provider: AuthProvider,
)

enum class AuthProvider(val key: String) {
    VK("vk"),
    YANDEX("yandex"),
    GOOGLE("google"),
}

/** Результат авторизации в наших терминах. */
sealed interface AuthResult {
    data class Success(val user: User) : AuthResult
    data class Error(val message: String) : AuthResult
    data object Cancelled : AuthResult
}
