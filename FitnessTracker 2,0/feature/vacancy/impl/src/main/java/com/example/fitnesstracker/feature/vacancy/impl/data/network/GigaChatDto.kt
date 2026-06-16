package com.example.fitnesstracker.feature.vacancy.impl.data.network

import com.google.gson.annotations.SerializedName

/** Ответ эндпоинта OAuth: токен доступа и время истечения (Unix ms). */
internal data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_at") val expiresAt: Long,
)

/** Одно сообщение диалога с ролью (system/user/assistant). */
internal data class ChatMessage(val role: String, val content: String)

/** Тело запроса к /chat/completions. */
internal data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val stream: Boolean = false,
    val temperature: Double = 0.7,
)

internal data class ChatChoice(val message: ChatMessage)
internal data class ChatResponse(val choices: List<ChatChoice>)
