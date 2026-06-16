package com.example.fitnesstracker.feature.profile.api.domain.repository

import com.example.fitnesstracker.feature.profile.api.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Контракт работы с профилем. За ним скрыт Firestore. Интерфейс лежит в
 * domain-пакете чистого модуля :api — реализация (Firestore) живёт в :impl.
 */
interface ProfileRepository {
    /** Создаёт/обновляет документ профиля (users/{id}). */
    suspend fun saveProfile(profile: UserProfile)
    /** Точечно обновляет FCM-токен (вызывается из onNewToken). */
    suspend fun updateFcmToken(userId: String, token: String)
    /** Чтение профиля с подпиской на обновления в реальном времени. */
    fun observeProfile(userId: String): Flow<UserProfile?>
}
