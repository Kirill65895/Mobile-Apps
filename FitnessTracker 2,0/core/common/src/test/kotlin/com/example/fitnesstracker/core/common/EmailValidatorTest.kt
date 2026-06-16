package com.example.fitnesstracker.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {

    @Test fun `valid gmail address passes`() {
        assertTrue(EmailValidator.isValidGoogleEmail("user.name@gmail.com"))
        assertTrue(EmailValidator.isValidGoogleEmail("  test@googlemail.com  "))
    }

    @Test fun `empty input is rejected`() {
        val r = EmailValidator.validateGoogleEmail("")
        assertTrue(r is EmailValidationResult.Invalid)
    }

    @Test fun `malformed email is rejected`() {
        assertFalse(EmailValidator.isValidGoogleEmail("not-an-email"))
        assertFalse(EmailValidator.isValidGoogleEmail("user@@gmail.com"))
        assertFalse(EmailValidator.isValidGoogleEmail("user@gmail"))
    }

    @Test fun `non-google domain is rejected`() {
        assertFalse(EmailValidator.isValidGoogleEmail("user@yandex.ru"))
        assertFalse(EmailValidator.isValidGoogleEmail("user@outlook.com"))
    }

    @Test fun `invalid result carries a message`() {
        val r = EmailValidator.validateGoogleEmail("user@yandex.ru")
        assertEquals(EmailValidationResult.Invalid("Требуется аккаунт Google (например, name@gmail.com)"), r)
    }
}
