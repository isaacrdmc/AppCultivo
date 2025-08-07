package com.example.cropmonitor.data.repository


import com.example.cropmonitor.data.models.ConfiguracionNotificacionDto
import com.example.cropmonitor.data.models.NotificacionDto
import com.example.cropmonitor.data.models.UpdateConfiguracionDto
import com.example.cropmonitor.data.api.NotificacionesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Repositorio que maneja la lógica de datos para las notificaciones.
class NotificacionesRepository(private val notificacionesApiService: NotificacionesApiService) {

    private val _notificaciones = MutableStateFlow<List<NotificacionDto>>(emptyList())
    val notificaciones: Flow<List<NotificacionDto>> = _notificaciones.asStateFlow()

    private val _cantidadNoLeidas = MutableStateFlow(0)
    val cantidadNoLeidas: Flow<Int> = _cantidadNoLeidas.asStateFlow()

    private val _configuracion = MutableStateFlow(ConfiguracionNotificacionDto())
    val configuracion: Flow<ConfiguracionNotificacionDto> = _configuracion.asStateFlow()

    suspend fun getNotificaciones(leidas: Boolean? = null) {
        try {
            _notificaciones.value = notificacionesApiService.getNotificaciones(leidas)
        } catch (e: Exception) {
            // Manejo de errores (por ejemplo, registrar, mostrar un mensaje)
            e.printStackTrace()
        }
    }

    suspend fun getCantidadNoLeidas() {
        try {
            val response = notificacionesApiService.getCantidadNotificacionesNoLeidas()
            _cantidadNoLeidas.value = response["cantidad"] ?: 0
        } catch (e: Exception) {
            // Manejo de errores
            e.printStackTrace()
            _cantidadNoLeidas.value = 0
        }
    }

    suspend fun marcarComoLeida(notificacionId: Int) {
        try {
            notificacionesApiService.marcarComoLeida(notificacionId)
            // Actualizar el estado localmente después de un éxito
            _notificaciones.update { list ->
                list.map { notif ->
                    if (notif.notificacionID == notificacionId) {
                        notif.copy(leida = true)
                    } else {
                        notif
                    }
                }
            }
            // Disminuir el contador de no leídas
            _cantidadNoLeidas.update { it - 1 }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getConfiguracion() {
        try {
            _configuracion.value = notificacionesApiService.getConfiguracion()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateConfiguracion(configDto: UpdateConfiguracionDto) {
        try {
            val updatedConfig = notificacionesApiService.updateConfiguracion(configDto)
            _configuracion.value = updatedConfig
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
