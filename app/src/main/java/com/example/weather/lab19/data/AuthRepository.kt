package com.example.weather.lab19.data

import android.content.Context
import com.example.weather.lab11.WeatherDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(context: Context) : AutoCloseable {
    private val db = WeatherDbHelper(context.applicationContext)

    suspend fun login(login: String, password: String): Boolean = withContext(Dispatchers.IO) {
        db.checkUser(login.trim(), password)
    }

    suspend fun register(login: String, password: String): Boolean = withContext(Dispatchers.IO) {
        db.registerUser(login.trim(), password)
    }

    override fun close() = db.close()
}
