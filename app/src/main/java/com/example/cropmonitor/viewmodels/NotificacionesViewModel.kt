package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.repository.NotificacionesRepository
import com.example.cropmonitor.data.models.ConfiguracionNotificacionDto
import com.example.cropmonitor.data.models.NotificacionDto
import com.example.cropmonitor.data.models.UpdateConfiguracionDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estados posibles de la UI para notificaciones y configuración
sealed interface NotificacionesUiState {
    data class Success(val notificaciones: List<NotificacionDto>) : NotificacionesUiState
    data class SuccessConfiguracion(val configuracion: ConfiguracionNotificacionDto) : NotificacionesUiState
    object Error : NotificacionesUiState
    object Loading : NotificacionesUiState
}

// ViewModel para gestionar las notificaciones y la configuración
class NotificacionesViewModel(private val notificacionesRepository: NotificacionesRepository) : ViewModel() {

    private val _notificacionesUiState = MutableStateFlow<NotificacionesUiState>(NotificacionesUiState.Loading)
    val notificacionesUiState: StateFlow<NotificacionesUiState> = _notificacionesUiState.asStateFlow()

    // Nuevo StateFlow para la configuración
    private val _configuracionUiState = MutableStateFlow<NotificacionesUiState>(NotificacionesUiState.Loading)
    val configuracionUiState: StateFlow<NotificacionesUiState> = _configuracionUiState.asStateFlow()

    val cantidadNoLeidas = notificacionesRepository.cantidadNoLeidas

    init {
        loadNotificaciones()
        loadCantidadNoLeidas()
        loadConfiguracion()
    }

    fun loadNotificaciones(leidas: Boolean? = null) {
        viewModelScope.launch {
            _notificacionesUiState.value = NotificacionesUiState.Loading
            try {
                notificacionesRepository.getNotificaciones(leidas)
                notificacionesRepository.notificaciones.collect { notifList ->
                    _notificacionesUiState.value = NotificacionesUiState.Success(notifList)
                }
            } catch (e: Exception) {
                _notificacionesUiState.value = NotificacionesUiState.Error
                e.printStackTrace()
            }
        }
    }

    private fun loadCantidadNoLeidas() {
        viewModelScope.launch {
            notificacionesRepository.getCantidadNoLeidas()
        }
    }

    // Método público para cargar la configuración
    fun loadConfiguracion() {
        viewModelScope.launch {
            _configuracionUiState.value = NotificacionesUiState.Loading
            try {
                notificacionesRepository.getConfiguracion()
                notificacionesRepository.configuracion.collect { config ->
                    _configuracionUiState.value = NotificacionesUiState.SuccessConfiguracion(config)
                }
            } catch (e: Exception) {
                _configuracionUiState.value = NotificacionesUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun marcarComoLeida(notificacionId: Int) {
        viewModelScope.launch {
            notificacionesRepository.marcarComoLeida(notificacionId)
        }
    }

    fun updateConfiguracion(configDto: UpdateConfiguracionDto) {
        viewModelScope.launch {
            notificacionesRepository.updateConfiguracion(configDto)
        }
    }
}
