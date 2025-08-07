package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cropmonitor.auth.TokenManager
import com.example.cropmonitor.data.repository.CultivosRepository
import com.example.cropmonitor.data.repository.ModulosRepository
import com.example.cropmonitor.network.AuthApiService
import java.lang.IllegalArgumentException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.cropmonitor.data.repository.UsuariosRepository

// NOTIFICATIONS REFACTOR
import com.example.cropmonitor.data.repository.NotificacionesRepository
// FIN NOTIFICATIONS REFACTOR

class ModulosViewModelFactory(
    private val modulosRepository: ModulosRepository,
    private val authService: AuthApiService,
    private val tokenManager: TokenManager,
    private val cultivosRepository: CultivosRepository,
    private val usuariosRepository: UsuariosRepository,
    // NOTIFICATIONS REFACTOR: Añadimos el repositorio de notificaciones
    private val notificacionesRepository: NotificacionesRepository
    // FIN NOTIFICATIONS REFACTOR
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()

        if (modelClass.isAssignableFrom(ModulosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ModulosViewModel(modulosRepository) as T
        }
        if (modelClass.isAssignableFrom(SensoresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SensoresViewModel(modulosRepository) as T
        }
        // CASO CORREGIDO: Inyectamos ambos repositorios
        if (modelClass.isAssignableFrom(SlotDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SlotDetailViewModel(modulosRepository, cultivosRepository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authService, tokenManager) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authService) as T
        }
        if (modelClass.isAssignableFrom(CultivosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CultivosViewModel(cultivosRepository) as T
        }
        if (modelClass.isAssignableFrom(CultivoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CultivoDetailViewModel(cultivosRepository, savedStateHandle) as T
        }
        if (modelClass.isAssignableFrom(FavoritosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritosViewModel(cultivosRepository) as T
        }
        if (modelClass.isAssignableFrom(AjustesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AjustesViewModel(usuariosRepository) as T
        }
        // NOTIFICATIONS REFACTOR: Agregamos el nuevo NotificacionesViewModel
        if (modelClass.isAssignableFrom(NotificacionesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificacionesViewModel(notificacionesRepository) as T
        }
        // FIN NOTIFICATIONS REFACTOR
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
