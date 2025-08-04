package com.example.cropmonitor.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.network.AuthApiService
import kotlinx.coroutines.launch
import RegisterRequestDto

class RegisterViewModel(
    private val authService: AuthApiService
) : ViewModel() {
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMensaje by mutableStateOf<String?>(null)
    var registroExitoso by mutableStateOf(false)

    fun register() {
        viewModelScope.launch {
            isLoading = true
            errorMensaje = null
            registroExitoso = false

            if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                errorMensaje = "Todos los campos son obligatorios."
                isLoading = false
                return@launch
            }

            try {
                val response = authService.register(RegisterRequestDto(nombre, correo, contrasena))
                if (response.isSuccessful) {
                    registroExitoso = true
                } else {
                    errorMensaje = "Error desconocido durante el registro."
                }
            } catch (e: Exception) {
                errorMensaje = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}