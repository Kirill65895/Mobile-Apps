package com.example.fitnesstracker.feature.vacancy.impl.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Перехватчик: добавляет в каждый запрос к API заголовок Authorization: Bearer <token>.
 * При 401 (токен протух) обновляет токен и повторяет запрос один раз. Благодаря этому
 * репозиторий не думает об авторизации.
 *
 * Примечание (как и в методичке): для простоты используется runBlocking. В реальном
 * приложении лучше асинхронные адаптеры корутин.
 */
internal class AuthInterceptor(
    private val tokenProvider: suspend () -> String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider() }
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        val response = chain.proceed(request)
        if (response.code == 401) {
            response.close()
            val fresh = runBlocking { tokenProvider() }
            val retried = chain.request().newBuilder()
                .header("Authorization", "Bearer $fresh")
                .build()
            return chain.proceed(retried)
        }
        return response
    }
}
