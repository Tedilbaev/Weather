package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import com.example.weather.lab1.MainActivity

class LabMenuActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_THEME = "theme_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_menu)

        findViewById<Button>(R.id.btnLab1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.btnLab2).setOnClickListener {
            startActivity(Intent(this, com.example.weather.lab2.LoginActivity::class.java))
        }

        findViewById<Button>(R.id.btnLab3).setOnClickListener {
            startActivity(Intent(this, com.example.weather.lab3.LoginActivity::class.java))
        }

        findViewById<Button>(R.id.btnLab4).setOnClickListener {
            startActivity(Intent(this, com.example.weather.lab4.LoginActivity::class.java))
        }

        findViewById<Button>(R.id.btnLab5).setOnClickListener {
            startActivity(Intent(this, com.example.weather.lab5.CityListActivity::class.java))
        }

        findViewById<Button>(R.id.btnLab6).setOnClickListener {
            startActivity(Intent(this, com.example.weather.lab6.LoginActivity::class.java))
        }

        findViewById<Button>(R.id.btnLab7).setOnClickListener {
            startActivity(Intent(this, com.example.weather.lab7.LoginActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener { anchor ->
            showSettingsMenu(anchor)
        }
    }

    private fun showSettingsMenu(anchor: View) {
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
}