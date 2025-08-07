package com.example.cropmonitor.data.models

import com.google.gson.annotations.SerializedName

// DTO para la lista de notificaciones.
// Usa @SerializedName para mapear las propiedades de la API.
data class NotificacionDto(
    @SerializedName("notificacionID") val notificacionID: Int,
    @SerializedName("tipoNotificacion") val tipoNotificacion: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("fechaHoraEnvio") val fechaHoraEnvio: String,
    @SerializedName("leida") val leida: Boolean,
    @SerializedName("cultivoID") val cultivoID: Int? = null,
    @SerializedName("cultivoNombre") val cultivoNombre: String? = null,
    @SerializedName("sensorID") val sensorID: Int? = null,
    @SerializedName("sensorNombre") val sensorNombre: String? = null
)

// DTO para la configuración de notificaciones del usuario.
data class ConfiguracionNotificacionDto(
    @SerializedName("frecuenciaRiego") val frecuenciaRiego: String? = null,
    @SerializedName("horarioNotificacion") val horarioNotificacion: String? = null,
    @SerializedName("activarRiegoAutomatico") val activarRiegoAutomatico: Boolean = false,
    @SerializedName("tipoAlertaSensor") val tipoAlertaSensor: String? = null,
    @SerializedName("habilitarRecomendacionesEstacionales") val habilitarRecomendacionesEstacionales: Boolean = true
)

// DTO para enviar la configuración actualizada.
data class UpdateConfiguracionDto(
    @SerializedName("frecuenciaRiego") val frecuenciaRiego: String,
    @SerializedName("horarioNotificacion") val horarioNotificacion: String,
    @SerializedName("activarRiegoAutomatico") val activarRiegoAutomatico: Boolean,
    @SerializedName("tipoAlertaSensor") val tipoAlertaSensor: String,
    @SerializedName("habilitarRecomendacionesEstacionales") val habilitarRecomendacionesEstacionales: Boolean
)
