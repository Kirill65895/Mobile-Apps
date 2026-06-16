package com.example.fitnesstracker.feature.vacancy.impl.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.fitnesstracker.core.common.CrashReporter
import com.example.fitnesstracker.feature.vacancy.api.domain.model.AnalysisResult
import com.example.fitnesstracker.feature.vacancy.api.domain.repository.VacancyAnalysisRepository
import com.example.fitnesstracker.feature.vacancy.impl.data.network.ChatMessage
import com.example.fitnesstracker.feature.vacancy.impl.data.network.ChatRequest
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaChatApi
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaChatAuthKey
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaChatConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Реализация анализа вакансий поверх GigaChat. Единственное место, связывающее
 * токен-менеджер, сеть и промпт. Без ключа (BuildConfig.GIGACHAT_AUTH_KEY) работает
 * в ДЕМО-режиме, поэтому экран доступен сразу; с ключом — реальные запросы к API.
 */
internal class VacancyAnalysisRepositoryImpl @Inject constructor(
    private val api: GigaChatApi,
    private val authKey: GigaChatAuthKey,
    private val crashReporter: CrashReporter,
    @ApplicationContext private val context: Context,
) : VacancyAnalysisRepository {

    override suspend fun analyze(vacancyText: String, skillsText: String): AnalysisResult {
        if (authKey.value.isBlank()) return demo(vacancyText, skillsText)
        if (!isOnline()) return AnalysisResult.Error("Нет подключения к интернету")

        // Системный промпт собирается подстановкой плейсхолдеров (пример форматирования строк).
        val systemPrompt = GigaChatConstants.SYSTEM_PROMPT
            .replace(GigaChatConstants.PLACEHOLDER_VACANCY, vacancyText)
            .replace(GigaChatConstants.PLACEHOLDER_SKILLS, skillsText)

        val request = ChatRequest(
            model = GigaChatConstants.MODEL,
            messages = listOf(
                ChatMessage(GigaChatConstants.ROLE_SYSTEM, systemPrompt),
                ChatMessage(GigaChatConstants.ROLE_USER, "Проанализируй вакансию с учётом моих навыков."),
            ),
        )
        return try {
            val text = api.chat(request).choices.firstOrNull()?.message?.content
            if (text.isNullOrBlank()) AnalysisResult.Error("Пустой ответ модели")
            else AnalysisResult.Success(text)
        } catch (e: HttpException) {
            crashReporter.recordException(e, mapOf("layer" to "VacancyRepository", "code" to e.code().toString()))
            AnalysisResult.Error(
                when (e.code()) {
                    401 -> "Ошибка авторизации (401): проверьте ключ GigaChat"
                    429 -> "Превышен лимит запросов/токенов, попробуйте позже"
                    in 500..599 -> "Ошибка сервера GigaChat (${e.code()})"
                    else -> "Ошибка запроса (${e.code()})"
                }
            )
        } catch (e: IOException) {
            AnalysisResult.Error("Нет сети или сервис недоступен")
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("layer" to "VacancyRepository"))
            AnalysisResult.Error("Не удалось выполнить анализ: ${e.message ?: "неизвестная ошибка"}")
        }
    }

    private fun isOnline(): Boolean {
        val cm = context.getSystemService(ConnectivityManager::class.java) ?: return true
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /** Демо-ответ без обращения к сети — чтобы экран работал без ключа GigaChat. */
    private fun demo(vacancy: String, skills: String): AnalysisResult = AnalysisResult.Success(
        """
        [ДЕМО-режим: ключ GigaChat не задан в local.properties]

        1. Оценка соответствия
        По указанным навыкам («${skills.take(60)}…») кандидат частично соответствует
        требованиям вакансии. Сильная сторона — базовый стек; пробел — нехватка опыта
        по части требований из описания.

        2. Что подтянуть
        • Технологии из вакансии, которых нет в навыках
        • Практику с тестированием и архитектурой
        • Инструменты CI/CD

        3. Вопросы работодателю
        • Какой стек используется в команде и почему?
        • Как организован процесс ревью и тестирования?
        • Есть ли наставничество для junior-разработчиков?

        (С реальным ключом GigaChat здесь будет ответ нейросети.)
        """.trimIndent()
    )
}
