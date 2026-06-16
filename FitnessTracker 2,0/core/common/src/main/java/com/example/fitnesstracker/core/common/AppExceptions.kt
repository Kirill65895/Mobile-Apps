package com.example.fitnesstracker.core.common

/**
 * Доменные исключения приложения. Осмысленные имена помогают группировать ошибки
 * в крашлитике (видно EmptyProfileException, а не a.b.c). Сохраняются в release
 * правилом R8: -keep public class * extends java.lang.Exception (proguard-rules.pro).
 */
class EmptyProfileException(message: String) : RuntimeException(message)
class InvalidWorkoutException(message: String) : RuntimeException(message)
