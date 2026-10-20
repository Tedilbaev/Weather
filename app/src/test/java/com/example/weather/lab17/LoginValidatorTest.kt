package com.example.weather.lab17

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoginValidatorTest {

    @Test
    fun emptyLogin_returnsEmptyLogin() {
        assertEquals(
            LoginValidator.Error.EMPTY_LOGIN,
            LoginValidator.validate("", "1234")
        )
    }

    @Test
    fun emptyPassword_returnsEmptyPassword() {
        assertEquals(
            LoginValidator.Error.EMPTY_PASSWORD,
            LoginValidator.validate("user", "  ")
        )
    }

    @Test
    fun shortPassword_returnsShortPassword() {
        assertEquals(
            LoginValidator.Error.SHORT_PASSWORD,
            LoginValidator.validate("user", "12")
        )
    }

    @Test
    fun validInput_returnsNull() {
        assertNull(LoginValidator.validate("user", "1234"))
    }
}