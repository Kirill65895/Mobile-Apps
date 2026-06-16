package com.example.fitnesstracker.feature.vacancy.impl.data.network

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/** Эндпоинт авторизации (OAuth 2.0). Возвращает короткоживущий access_token (~30 мин). */
internal interface GigaOAuthApi {
    @FormUrlEncoded
    @POST(GigaChatConstants.OAUTH_PATH)
    suspend fun getToken(
        @Header("Authorization") basicAuth: String,   // "Basic <ключ из ЛК GigaChat>"
        @Header("RqUID") requestId: String,
        @Field("scope") scope: String = GigaChatConstants.SCOPE,
    ): TokenResponse
}

/**
 * Основной API GigaChat. Токен НЕ передаётся параметром — его добавляет
 * OkHttp Interceptor (заголовок Authorization), поэтому репозиторий не думает о токенах.
 */
internal interface GigaChatApi {
    @POST(GigaChatConstants.CHAT_PATH)
    suspend fun chat(@Body request: ChatRequest): ChatResponse
}
