package com.example.fitnesstracker.core.common

/**
 * Универсальная обёртка результата операций бизнес-логики.
 * Лежит в :core:common, поэтому переиспользуется всеми слоями и фичами.
 */
sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(val throwable: Throwable) : DataResult<Nothing>
}

inline fun <T, R> DataResult<T>.map(transform: (T) -> R): DataResult<R> = when (this) {
    is DataResult.Success -> DataResult.Success(transform(data))
    is DataResult.Error -> this
}
