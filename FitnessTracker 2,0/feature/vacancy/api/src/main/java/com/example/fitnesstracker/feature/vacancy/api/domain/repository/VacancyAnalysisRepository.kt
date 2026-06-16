package com.example.fitnesstracker.feature.vacancy.api.domain.repository

import com.example.fitnesstracker.feature.vacancy.api.domain.model.AnalysisResult

/**
 * Контракт анализа вакансии. За ним скрыт GigaChat (OAuth 2.0, Retrofit, кеш токена).
 * Интерфейс в domain-пакете чистого :api модуля; реализация — в :impl.
 */
interface VacancyAnalysisRepository {
    suspend fun analyze(vacancyText: String, skillsText: String): AnalysisResult
}
