package com.example.cropmonitor.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.auth.TokenManager
import com.example.cropmonitor.network.AuthApiService
import kotlinx.coroutines.launch
import LoginRequestDto

class LoginViewModel(
    private val authService: AuthApiService,
    private val tokenManager: TokenManager
) : ViewModel() {
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMensaje by mutableStateOf<String?>(null)

    fun login(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMensaje = null
            try {
                val response = authService.login(LoginRequestDto(correo, contrasena))
                if (response.isSuccessful) {
                    val token = response.body()?.jwtToken
                    if (token != null) {
                        tokenManager.saveToken(token)
                        onLoginSuccess()
                    } else {
                        errorMensaje = "Token de autenticación no recibido."
                    }
                } else {
                    errorMensaje = "Credenciales inválidas. Correo o contraseña incorrectos."
                }
            } catch (e: Exception) {
                errorMensaje = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}