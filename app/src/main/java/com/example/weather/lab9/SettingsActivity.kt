package com.example.weather.lab9

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.weather.R

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_VOLUME = "volume"
        private const val KEY_DARK_THEME = "theme_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab9_activity_settings)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val switchNotifications = findViewById<SwitchCompat>(R.id.switchNotifications)
        val switchDarkTheme = findViewById<SwitchCompat>(R.id.switchDarkTheme)
        val seekBarVolume = findViewById<SeekBar>(R.id.seekBarVolume)
        val tvVolumeValue = findViewById<TextView>(R.id.tvVolumeValue)
        val mode = prefs.getInt(KEY_DARK_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        switchNotifications.isChecked = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        switchDarkTheme.isChecked = (mode == AppCompatDelegate.MODE_NIGHT_YES)
        val volume = prefs.getInt(KEY_VOLUME, 50)
        seekBarVolume.progress = volume
        tvVolumeValue.text = volume.toString()

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, isChecked).apply()
            Toast.makeText(
                this,
                if (isChecked) "Уведомления включены" else "Уведомления выключены",
                Toast.LENGTH_SHORT
            ).show()
        }

        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
            prefs.edit().putInt(KEY_DARK_THEME, mode).apply()   // Int, не Boolean
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvVolumeValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                prefs.edit().putInt(KEY_VOLUME, seekBar?.progress ?: 50).apply()
            }
        })
    }
}