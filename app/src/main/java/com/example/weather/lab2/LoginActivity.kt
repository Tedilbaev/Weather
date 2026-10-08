package com.example.weather.lab2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R

class LoginActivity : AppCompatActivity() {
    private var useConstraint = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchLayout()
    }

    private fun switchLayout() {
        setContentView(if (useConstraint) R.layout.lab2_activity_login else R.layout.lab2_activity_login_linear)
        findViewById<Button>(R.id.btnSwitchLayout).setOnClickListener {
            toggleLayout()
        }
    }

    fun toggleLayout() {
        useConstraint = !useConstraint
        switchLayout()
    }
}