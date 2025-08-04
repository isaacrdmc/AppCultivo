package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.models.MedidorSlotDto
import com.example.cropmonitor.data.repository.ModulosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class SensoresViewModel(
    private val modulosRepository: ModulosRepository
) : ViewModel() {

    // CORREGIDO: Ahora el StateFlow maneja directamente la lista de MedidorSlotDto
    private val _medidores = MutableStateFlow<List<MedidorSlotDto>>(emptyList())
    val medidores: StateFlow<List<MedidorSlotDto>> = _medidores

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSensores(moduloId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _medidores.value = emptyList()

            try {
                // Llama al repositorio que ya devuelve la lista agrupada
                val medidoresConSensores = modulosRepository.getSensoresByModulo(moduloId)
                _medidores.value = medidoresConSensores
            } catch (e: IOException) {
                _error.value = "Error de red: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Error desconocido: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}