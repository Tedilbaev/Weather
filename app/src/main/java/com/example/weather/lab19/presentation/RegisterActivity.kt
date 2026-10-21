package com.example.weather.lab19.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.lab19.viewmodel.AuthState
import com.example.weather.lab19.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab19_activity_register)
        title = getString(R.string.lab19_title)

        val etLogin = findViewById<EditText>(R.id.etLab19RegLogin)
        val etPassword = findViewById<EditText>(R.id.etLab19RegPassword)
        val etConfirm = findViewById<EditText>(R.id.etLab19RegConfirm)
        val tvResult = findViewById<TextView>(R.id.tvLab19AuthResult)

        findViewById<Button>(R.id.btnLab19RegisterConfirm).setOnClickListener {
            viewModel.register(
                etLogin.text.toString(),
                etPassword.text.toString(),
                etConfirm.text.toString()
            )
        }

        findViewById<Button>(R.id.btnLab19BackToLogin).setOnClickListener { finish() }

        viewModel.state.observe(this) { state ->
            when (state) {
                AuthState.Idle, AuthState.Loading -> Unit
                AuthState.Success -> {
                    Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is AuthState.Error -> {
                    val message = getString(state.messageResId)
                    tvResult.text = message
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}