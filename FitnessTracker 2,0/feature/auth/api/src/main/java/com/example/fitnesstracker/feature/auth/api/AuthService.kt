package com.example.fitnesstracker.feature.auth.api

import android.app.Activity

/**
 * ФАСАД авторизации (Лаб. №6). Скрывает за собой VK SDK, Yandex LoginSDK и работу
 * с защищённым хранилищем токенов. Код приложения (LoginViewModel, :app) зависит
 * ТОЛЬКО от этого интерфейса. Если завтра VK сменит API — правим одну реализацию.
 */
interface AuthService {
    suspend fun loginWithVk(activity: Activity): AuthResult
    suspend fun loginWithYandex(activity: Activity): AuthResult

    /** Регистрация/вход по Google-аккаунту. email уже должен быть провалидирован. */
    suspend fun loginWithGoogle(activity: Activity, email: String): AuthResult
    fun logout()
    /** Возвращает сохранённого пользователя или null (используется для пропуска экрана входа). */
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
}
