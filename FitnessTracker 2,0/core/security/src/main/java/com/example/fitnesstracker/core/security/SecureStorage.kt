package com.example.fitnesstracker.core.security

/**
 * Фасад над защищённым хранилищем. Остальной код вызывает putString/getString,
 * НЕ зная, что под капотом — шифрование. Это позволяет в будущем заменить
 * реализацию (например, Keystore + Room/SQLCipher) в одном месте.
 */
interface SecureStorage {
    fun putString(key: String, value: String)
    fun getString(key: String): String?
    fun putLong(key: String, value: Long)
    fun getLong(key: String, default: Long = 0L): Long
    fun remove(key: String)
    fun clear()
}
