package com.example.weather.lab12

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.lab11.WeatherDbHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: WeatherDbHelper

    companion object {
        private const val MIN_PASSWORD_LENGTH = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab12_activity_register)
        db = WeatherDbHelper(this)

        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirm = findViewById<EditText>(R.id.etPasswordConfirm)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            handleRegister(
                etLogin.text.toString(),
                etPassword.text.toString(),
                etConfirm.text.toString(),
                tvResult
            )
        }

        findViewById<Button>(R.id.btnBackToLogin).setOnClickListener {
            finish()
        }
    }

    private fun handleRegister(
        login: String,
        password: String,
        confirm: String,
        tvResult: TextView
    ) {
        val error = validate(login, password, confirm)
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            tvResult.text = error
            return
        }

        if (db.registerUser(login, password)) {
            Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
            finish() // вернуться на LoginActivity
        } else {
            val msg = getString(R.string.register_failed)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            tvResult.text = msg
        }
    }

    private fun validate(login: String, password: String, confirm: String): String? {
        return when {
            login.isBlank() -> getString(R.string.error_empty_login)
            password.isBlank() -> getString(R.string.error_empty_password)
            password.length < MIN_PASSWORD_LENGTH ->
                getString(R.string.error_short_password, MIN_PASSWORD_LENGTH)
            password != confirm -> getString(R.string.error_password_mismatch)
            else -> null
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}