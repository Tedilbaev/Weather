package com.example.weather.lab3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R


class LoginActivity : AppCompatActivity() {
    private var useConstraint = true

    companion object {
        private const val TAG = "Lab3_LoginActivity"
        private const val MIN_PASSWORD_LENGTH = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        renderScreen()
    }

    private fun renderScreen() {
        setContentView(if (useConstraint) R.layout.lab3_activity_login else R.layout.lab3_activity_login_linear)
        bindViews()
    }

    private fun bindViews() {
        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            handleLoginClick(etLogin.text.toString(), etPassword.text.toString(), tvResult)
        }

        findViewById<Button>(R.id.btnSwitchLayout).setOnClickListener {
            toggleLayout()
        }
    }

    private fun handleLoginClick(login: String, password: String, tvResult: TextView) {
        val message = validateInput(login, password)

        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            tvResult.text = message
            return
        }

        val successMessage = "Добро пожаловать, $login!"
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
        tvResult.text = "Логин: $login\nПароль: ${"*".repeat(password.length)}"
    }

    private fun validateInput(login: String, password: String): String? {
        return when {
            login.isBlank() -> "Введите логин"
            password.isBlank() -> "Введите пароль"
            password.length < MIN_PASSWORD_LENGTH -> "Пароль слишком короткий (минимум ${MIN_PASSWORD_LENGTH} символа)"
            else -> null
        }
    }

    private fun toggleLayout() {
        useConstraint = !useConstraint
        renderScreen()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}