package com.example.cropmonitor.data.models

import com.google.gson.annotations.SerializedName
//Modelo para el perfil del usuario autenticado
data class UsuarioProfileDto(
    @SerializedName("UsuarioID") val usuarioID: Int,
    @SerializedName("NombreUsuario") val nombreUsuario: String,
    @SerializedName("Correo") val correo: String
)
// Modelo para cambiar la contraseña del usuario
data class ChangePasswordDto(
    @SerializedName("CurrentPassword") val currentPassword: String,
    @SerializedName("NewPassword") val newPassword: String,
    @SerializedName("ConfirmNewPassword") val confirmNewPassword: String
)