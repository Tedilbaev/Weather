package com.example.weather

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private var useConstraint = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchLayout()
    }

    private fun switchLayout() {
        setContentView(if (useConstraint) R.layout.activity_login else R.layout.activity_login_linear)
        findViewById<Button>(R.id.btnSwitchLayout).setOnClickListener {
            toggleLayout()
        }
    }

    fun toggleLayout() {
        useConstraint = !useConstraint
        switchLayout()
    }
}