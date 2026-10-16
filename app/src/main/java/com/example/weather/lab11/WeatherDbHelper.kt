package com.example.weather.lab11

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.security.SecureRandom

data class City(
    val id: Long = 0,
    val name: String,
    val description: String = ""
)

class WeatherDbHelper(context: Context) : SQLiteOpenHelper(
    context, "weather.db", null, 2
) {
    companion object {
        const val TABLE_CITIES = "cities"
        const val TABLE_USERS = "users"

        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_DESCRIPTION = "description"

        const val COL_LOGIN = "login"
        const val COL_PASSWORD_HASH = "password_hash"
        const val COL_SALT = "salt"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_CITIES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_DESCRIPTION TEXT
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_LOGIN TEXT NOT NULL UNIQUE,
                $COL_PASSWORD_HASH TEXT NOT NULL,
                $COL_SALT TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CITIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addCity(name: String, description: String = ""): Long {
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_DESCRIPTION, description)
        }
        return writableDatabase.insert(TABLE_CITIES, null, values)
    }

    fun updateCity(id: Long, name: String, description: String): Int {
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_DESCRIPTION, description)
        }
        return writableDatabase.update(
            TABLE_CITIES,
            values,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getAllCities(): List<City> {
        val list = mutableListOf<City>()
        readableDatabase.query(
            TABLE_CITIES,
            arrayOf(COL_ID, COL_NAME, COL_DESCRIPTION),
            null, null, null, null,
            "$COL_NAME ASC"
        ).use { c ->
            while (c.moveToNext()) {
                list.add(
                    City(
                        id = c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                        name = c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                        description = c.getString(c.getColumnIndexOrThrow(COL_DESCRIPTION))
                    )
                )
            }
        }
        return list
    }

    fun deleteCity(id: Long): Int =
        writableDatabase.delete(TABLE_CITIES, "$COL_ID = ?", arrayOf(id.toString()))

    fun registerUser(login: String, password: String): Boolean {
        if (login.isBlank() || password.isBlank()) return false
        if (userExists(login)) return false

        val salt = generateSalt()
        val hash = hashPassword(password, salt)

        val values = ContentValues().apply {
            put(COL_LOGIN, login.trim())
            put(COL_PASSWORD_HASH, hash)
            put(COL_SALT, salt)
        }
        return writableDatabase.insert(TABLE_USERS, null, values) != -1L
    }

    fun checkUser(login: String, password: String): Boolean {
        readableDatabase.query(
            TABLE_USERS,
            arrayOf(COL_PASSWORD_HASH, COL_SALT),
            "$COL_LOGIN = ?",
            arrayOf(login.trim()),
            null, null, null
        ).use { c ->
            if (!c.moveToFirst()) return false
            val storedHash = c.getString(c.getColumnIndexOrThrow(COL_PASSWORD_HASH))
            val salt = c.getString(c.getColumnIndexOrThrow(COL_SALT))
            return hashPassword(password, salt) == storedHash
        }
    }

    fun userExists(login: String): Boolean {
        readableDatabase.query(
            TABLE_USERS,
            arrayOf(COL_ID),
            "$COL_LOGIN = ?",
            arrayOf(login.trim()),
            null, null, null
        ).use { return it.moveToFirst() }
    }

    private fun generateSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest("$salt$password".toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}