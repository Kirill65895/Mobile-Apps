package com.example.fitnesstracker.feature.profile.api.domain.model

/** Профиль пользователя, хранимый в Firestore (коллекция users, документ = userId). */
data class UserProfile(
    val id: String,
    val name: String,
    val email: String?,
    val fcmToken: String?,
    val updatedAtMillis: Long,
)
