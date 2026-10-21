package com.example.weather.lab19.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.lab19.viewmodel.AuthState
import com.example.weather.lab19.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private var useConstraint = true
    private val viewModel: AuthViewModel by viewModels()

    companion object {
        private const val TAG = "Lab19_LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        renderScreen()
    }

    private fun renderScreen() {
        setContentView(
            if (useConstraint) R.layout.lab19_activity_login_constraint
            else R.layout.lab19_activity_login_linear
        )
        title = getString(R.string.lab19_title)
        bindViews()
    }

    private fun bindViews() {
        val etLogin = findViewById<EditText>(R.id.etLab19Login)
        val etPassword = findViewById<EditText>(R.id.etLab19Password)
        val tvResult = findViewById<TextView>(R.id.tvLab19AuthResult)

        findViewById<Button>(R.id.btnLab19Login).setOnClickListener {
            viewModel.login(etLogin.text.toString(), etPassword.text.toString())
        }

        findViewById<Button>(R.id.btnLab19Register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<Button>(R.id.btnLab19SwitchLayout).setOnClickListener {
            useConstraint = !useConstraint
            renderScreen()
        }

        viewModel.state.observe(this) { state ->
            when (state) {
                AuthState.Idle, AuthState.Loading -> Unit
                AuthState.Success -> {
                    Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, CityListActivity::class.java))
                    finish()
                }
                is AuthState.Error -> {
                    val message = getString(
                        state.messageResId,
                        AuthViewModel.MIN_PASSWORD_LENGTH
                    )
                    tvResult.text = message
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}