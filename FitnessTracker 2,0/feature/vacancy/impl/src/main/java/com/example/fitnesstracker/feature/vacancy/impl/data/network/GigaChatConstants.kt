package com.example.fitnesstracker.feature.vacancy.impl.data.network

/** Константы GigaChat — чтобы не дублировать «магические строки» (см. примечания методички). */
internal object GigaChatConstants {
    const val OAUTH_BASE_URL = "https://ngw.devices.sberbank.ru:9443/"
    const val API_BASE_URL = "https://gigachat.devices.sberbank.ru/"
    const val OAUTH_PATH = "api/v2/oauth"
    const val CHAT_PATH = "api/v1/chat/completions"
    const val SCOPE = "GIGACHAT_API_PERS"
    const val MODEL = "GigaChat"

    const val ROLE_SYSTEM = "system"
    const val ROLE_USER = "user"

    // Ключи для хранения токена в SecureStorage (EncryptedSharedPreferences).
    const val KEY_ACCESS_TOKEN = "gigachat_access_token"
    const val KEY_EXPIRES_AT = "gigachat_expires_at"

    // Плейсхолдеры системного промпта (подставляются через replace в коде).
    const val PLACEHOLDER_VACANCY = "{vacancyText}"
    const val PLACEHOLDER_SKILLS = "{skillsText}"

    val SYSTEM_PROMPT = """
        Ты — опытный IT-карьерный консультант. Проанализируй, насколько кандидат
        подходит под вакансию, и ответь СТРОГО тремя блоками с заголовками:

        1. Оценка соответствия — насколько навыки совпадают с требованиями, сильные стороны и явные пробелы.
        2. Что подтянуть — 2–3 конкретные темы/технологии, которые стоит изучить перед откликом.
        3. Вопросы работодателю — 2–3 уточняющих вопроса для собеседования.

        Вакансия:
        $PLACEHOLDER_VACANCY

        Навыки кандидата:
        $PLACEHOLDER_SKILLS
    """.trimIndent()
}
