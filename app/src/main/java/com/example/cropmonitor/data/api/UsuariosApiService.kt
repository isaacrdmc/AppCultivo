package com.example.cropmonitor.data.api


import com.example.cropmonitor.data.models.ChangePasswordDto
import com.example.cropmonitor.data.models.UsuarioProfileDto
import retrofit2.Response
import retrofit2.http.*

interface UsuariosApiService {

    // Endpoint para obtener el perfil del usuario autenticado
    @GET("usuarios/me")
    suspend fun getUserProfile(): Response<UsuarioProfileDto>

    // Endpoint para cambiar la contraseña del usuario
    @PUT("usuarios/me/password")
    suspend fun changePassword(@Body changePasswordDto: ChangePasswordDto): Response<Unit>

    // Endpoint para eliminar la cuenta del usuario
    @DELETE("usuarios/me")
    suspend fun deleteAccount(): Response<Unit>
}