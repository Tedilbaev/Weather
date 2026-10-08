package com.example.weather.lab8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.content.edit
import com.example.weather.R


class LoginActivity : AppCompatActivity() {
    private var useConstraint = true

    companion object {
        private const val TAG = "Lab8_LoginActivity"
        private const val MIN_PASSWORD_LENGTH = 4
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_SELECTED_CITY = "extra_selected_city"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        renderScreen()
    }

    private fun renderScreen() {
        setContentView(if (useConstraint) R.layout.lab6_activity_login else R.layout.lab6_activity_login_linear)
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



    private val cityListLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedCity = result.data?.getStringExtra(EXTRA_SELECTED_CITY)
            if (selectedCity != null) {
                findViewById<TextView>(R.id.tvResult).text =
                    getString(R.string.selected_city_template, selectedCity)
            }
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
        intent.putExtra(EXTRA_LOGIN, login)
        cityListLauncher.launch(intent)
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