package com.example.fitnesstracker.feature.vacancy.api.domain.model

/** Результат анализа вакансии в наших терминах (без типов сети/SDK). */
sealed interface AnalysisResult {
    /** Текст рекомендаций от модели (три блока: соответствие, что подтянуть, вопросы). */
    data class Success(val text: String) : AnalysisResult
    /** Ошибка с понятным пользователю сообщением. */
    data class Error(val message: String) : AnalysisResult
}
