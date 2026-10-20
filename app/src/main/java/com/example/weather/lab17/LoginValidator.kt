package com.example.weather.lab17

object LoginValidator {

    const val MIN_PASSWORD_LENGTH = 4

    enum class Error {
        EMPTY_LOGIN,
        EMPTY_PASSWORD,
        SHORT_PASSWORD
    }

    /** null = всё ок */
    fun validate(login: String, password: String): Error? {
        return when {
            login.isBlank() -> Error.EMPTY_LOGIN
            password.isBlank() -> Error.EMPTY_PASSWORD
            password.length < MIN_PASSWORD_LENGTH -> Error.SHORT_PASSWORD
            else -> null
        }
    }
}