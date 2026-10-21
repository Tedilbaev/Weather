package com.example.weather.lab19.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.R
import com.example.weather.lab19.data.AuthRepository
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data object Success : AuthState
    data class Error(val messageResId: Int) : AuthState
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    private val _state = MutableLiveData<AuthState>(AuthState.Idle)
    val state: LiveData<AuthState> = _state

    fun login(login: String, password: String) {
        if (!validateCredentials(login, password)) return
        executeAuth(R.string.login_failed) { repository.login(login, password) }
    }

    fun register(login: String, password: String, confirmation: String) {
        if (!validateCredentials(login, password)) return
        if (password != confirmation) {
            _state.value = AuthState.Error(R.string.error_password_mismatch)
            return
        }
        executeAuth(R.string.register_failed) { repository.register(login, password) }
    }

    private fun validateCredentials(login: String, password: String): Boolean {
        val error = when {
            login.isBlank() -> R.string.error_empty_login
            password.isBlank() -> R.string.error_empty_password
            password.length < MIN_PASSWORD_LENGTH -> com.example.weather.R.string.error_short_password
            else -> null
        }
        if (error != null) {
            _state.value = AuthState.Error(error)
            return false
        }
        return true
    }

    private fun executeAuth(errorResId: Int, action: suspend () -> Boolean) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val success = action()
            _state.value = if (success) AuthState.Success
            else AuthState.Error(errorResId)
        }
    }

    override fun onCleared() {
        repository.close()
        super.onCleared()
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 4
    }
}
