package com.example.weather.lab17

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.lab11.WeatherDbHelper
import com.example.weather.lab12.CityListActivity
import com.example.weather.lab12.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private var useConstraint = true
    private lateinit var db: WeatherDbHelper

    companion object {
        private const val TAG = "Lab17_LoginActivity"
        private const val MIN_PASSWORD_LENGTH = 4
        const val EXTRA_LOGIN = "extra_login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        db = WeatherDbHelper(this)
        renderScreen()
    }

    private fun renderScreen() {
        setContentView(
            if (useConstraint) R.layout.lab12_activity_login
            else R.layout.lab12_activity_login_linear
        )
        bindViews()
    }

    private fun bindViews() {
        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            handleLogin(etLogin.text.toString(), etPassword.text.toString(), tvResult)
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<Button>(R.id.btnSwitchLayout).setOnClickListener {
            useConstraint = !useConstraint
            renderScreen()
        }
    }

    private fun handleLogin(login: String, password: String, tvResult: TextView) {
        val error = validateInput(login, password)
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            tvResult.text = error
            return
        }

        if (db.checkUser(login, password)) {
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, CityListActivity::class.java).putExtra(
                    EXTRA_LOGIN,
                    login.trim()
                )
            )
        } else {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
            tvResult.text = getString(R.string.login_failed)
        }
    }

    private fun validateInput(login: String, password: String): String? {
        return when (LoginValidator.validate(login, password)) {
            LoginValidator.Error.EMPTY_LOGIN -> getString(R.string.error_empty_login)
            LoginValidator.Error.EMPTY_PASSWORD -> getString(R.string.error_empty_password)
            LoginValidator.Error.SHORT_PASSWORD ->
                getString(R.string.error_short_password, LoginValidator.MIN_PASSWORD_LENGTH)
            null -> null
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}