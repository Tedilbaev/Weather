package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.content.edit


class LoginActivity : AppCompatActivity() {
    private var useConstraint = true

    companion object {
        private const val TAG = "LoginActivity"
        private const val MIN_PASSWORD_LENGTH = 4
        private const val PREFS_NAME = "app_settings"
        private const val KEY_THEME = "theme_mode"
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

        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener { anchor ->
            showSettingsMenu(anchor)
        }
    }

    private fun showSettingsMenu(anchor: android.view.View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.menu_main, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_language -> {
                    showLanguageDialog()
                    true
                }
                R.id.action_theme -> {
                    showThemeDialog()
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Русский", "English")
        val codes = arrayOf("ru", "en")

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_choose_language)
            .setItems(languages) { _, which ->
                val localeList = LocaleListCompat.forLanguageTags(codes[which])
                AppCompatDelegate.setApplicationLocales(localeList)
            }
            .show()
    }

    private fun showThemeDialog() {
        val themes = arrayOf(
            getString(R.string.theme_light),
            getString(R.string.theme_dark),
            getString(R.string.theme_system)
        )

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_choose_theme)
            .setItems(themes) { _, which ->
                val mode = when (which) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                saveTheme(mode)
                AppCompatDelegate.setDefaultNightMode(mode)
            }
            .show()
    }

    private fun saveTheme(mode: Int) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit {
                putInt(KEY_THEME, mode)
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