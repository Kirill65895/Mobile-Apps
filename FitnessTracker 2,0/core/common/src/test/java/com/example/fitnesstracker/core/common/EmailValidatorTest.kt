package com.example.fitnesstracker.core.common

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {

    @Test fun `valid gmail address passes`() {
        assertTrue(EmailValidator.isValidGoogleEmail("ivan.petrov@gmail.com"))
        assertTrue(EmailValidator.isValidGoogleEmail("  user@googlemail.com  "))
    }

    @Test fun `non-google domain is rejected`() {
        assertFalse(EmailValidator.isValidGoogleEmail("user@yandex.ru"))
        assertFalse(EmailValidator.isValidGoogleEmail("user@mail.ru"))
    }

    @Test fun `malformed email is rejected`() {
        assertFalse(EmailValidator.isValidGoogleEmail(""))
        assertFalse(EmailValidator.isValidGoogleEmail("not-an-email"))
        assertFalse(EmailValidator.isValidGoogleEmail("user@gmail"))
    }
}
