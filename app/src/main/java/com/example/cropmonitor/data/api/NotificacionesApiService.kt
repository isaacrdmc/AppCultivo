package com.example.cropmonitor.data.api

import com.example.cropmonitor.data.models.ConfiguracionNotificacionDto
import com.example.cropmonitor.data.models.NotificacionDto
import com.example.cropmonitor.data.models.UpdateConfiguracionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificacionesApiService {
    @GET("Notificaciones")
    suspend fun getNotificaciones(@Query("leida") leida: Boolean?): List<NotificacionDto>

    @GET("Notificaciones/cantidad-no-leidas")
    suspend fun getCantidadNotificacionesNoLeidas(): Map<String, Int>

    @POST("Notificaciones/{notificacionId}/marcar-como-leida")
    suspend fun marcarComoLeida(@Path("notificacionId") notificacionId: Int)

    @GET("Notificaciones/configuracion")
    suspend fun getConfiguracion(): ConfiguracionNotificacionDto

    @PUT("Notificaciones/configuracion")
    suspend fun updateConfiguracion(@Body configDto: UpdateConfiguracionDto): ConfiguracionNotificacionDto
}
