package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    private var useConstraint = true

    companion object {
        private const val TAG = "LoginActivity"
        private const val MIN_PASSWORD_LENGTH = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        renderScreen()
    }

    private fun renderScreen() {
        setContentView(if (useConstraint) R.layout.activity_login else R.layout.activity_login_linear)
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

        val successMessage = getString(R.string.welcome_message, login)
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CityListActivity::class.java)
        startActivity(intent)
    }

    private fun validateInput(login: String, password: String): String? {
        return when {
            login.isBlank() -> getString(R.string.error_empty_login)
            password.isBlank() -> getString(R.string.error_empty_password)
            password.length < MIN_PASSWORD_LENGTH -> getString(R.string.error_short_password, MIN_PASSWORD_LENGTH)
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