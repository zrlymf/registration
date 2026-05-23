package com.example.registration.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var loginError by mutableStateOf<String?>(null)
        private set
    var isPasswordVisible by mutableStateOf(false)
        private set

    private val CREDENTIAL_EMAIL = "admin.ditmawa@its.ac.id"
    private val CREDENTIAL_PASSWORD = "its123"

    fun updateUsername(input: String) {
        username = input
        loginError = null
    }

    fun updatePassword(input: String) {
        password = input
        loginError = null
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun performLogin(): Boolean {
        if (username == CREDENTIAL_EMAIL && password == CREDENTIAL_PASSWORD) {
            loginError = null
            return true
        } else {
            loginError = "Email atau Password Admin Ditmawa salah!"
            return false
        }
    }
}