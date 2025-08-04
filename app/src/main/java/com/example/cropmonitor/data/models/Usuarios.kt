package com.example.cropmonitor.data.models

import com.google.gson.annotations.SerializedName
//Modelo para el perfil del usuario autenticado
data class UsuarioProfileDto(
    @SerializedName("usuarioID") val usuarioID: Int,
    @SerializedName("nombreUsuario") val nombreUsuario: String,
    @SerializedName("correo") val correo: String
)
// Modelo para cambiar la contraseña del usuario
data class ChangePasswordDto(
    @SerializedName("currentPassword") val currentPassword: String,
    @SerializedName("newPassword") val newPassword: String,
    @SerializedName("confirmNewPassword") val confirmNewPassword: String
)