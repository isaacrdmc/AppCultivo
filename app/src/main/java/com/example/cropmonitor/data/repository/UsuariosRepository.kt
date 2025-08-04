package com.example.cropmonitor.data.repository

import com.example.cropmonitor.data.models.ChangePasswordDto
import com.example.cropmonitor.data.models.UsuarioProfileDto
import com.example.cropmonitor.data.api.UsuariosApiService
import java.io.IOException

class UsuariosRepository(private val apiService: UsuariosApiService) {

    suspend fun getUserProfile(): Result<UsuarioProfileDto> {
        return try {
            val response = apiService.getUserProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor."))
            } else {
                Result.failure(IOException("Error al cargar el perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(dto: ChangePasswordDto): Result<Unit> {
        return try {
            val response = apiService.changePassword(dto)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(IOException("Error al cambiar contraseña: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val response = apiService.deleteAccount()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(IOException("Error al eliminar la cuenta: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}