package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.models.CultivoListDto
import com.example.cropmonitor.data.models.MedidorSlotDto
import com.example.cropmonitor.data.repository.CultivosRepository
import com.example.cropmonitor.data.repository.ModulosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// El estado completo de la UI para la pantalla de detalle del slot, incluyendo la lógica del diálogo
sealed interface SlotDetailUiState {
    data object Loading : SlotDetailUiState
    data class Success(
        val medidorSlot: MedidorSlotDto,
        val cultivosDisponibles: List<CultivoListDto> = emptyList(),
        val showCultivoSelectionDialog: Boolean = false,
        val isAssigningCultivo: Boolean = false
    ) : SlotDetailUiState
    data class Error(val message: String) : SlotDetailUiState
}

class SlotDetailViewModel(
    private val modulosRepository: ModulosRepository,
    private val cultivosRepository: CultivosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SlotDetailUiState>(SlotDetailUiState.Loading)
    val uiState: StateFlow<SlotDetailUiState> = _uiState.asStateFlow()

    fun loadSlotDetail(moduloId: Int, medidorSlotIndex: Int) {
        viewModelScope.launch {
            _uiState.value = SlotDetailUiState.Loading
            try {
                // Obtiene todos los slots y filtra en el cliente
                val fetchedSlots = modulosRepository.getSensoresByModulo(moduloId)
                val slot = fetchedSlots.find { it.medidorSlotIndex == medidorSlotIndex }

                if (slot != null) {
                    _uiState.value = SlotDetailUiState.Success(slot)
                } else {
                    _uiState.value = SlotDetailUiState.Error("Slot no encontrado.")
                }
            } catch (e: Exception) {
                _uiState.value = SlotDetailUiState.Error("Error al cargar los datos del slot: ${e.message}")
            }
        }
    }

    // Carga los cultivos disponibles para el diálogo
    fun loadCultivosDisponibles() {
        viewModelScope.launch {
            try {
                val result = cultivosRepository.getCultivos()
                val cultivos = if (result.isSuccess) result.getOrNull() ?: emptyList() else emptyList()
                _uiState.update { currentState ->
                    if (currentState is SlotDetailUiState.Success) {
                        currentState.copy(cultivosDisponibles = cultivos)
                    } else currentState
                }
            } catch (e: Exception) {
                // Podrías manejar el error aquí si quieres
            }
        }
    }

    // Lógica para asignar el cultivo
    fun asignarCultivo(moduloId: Int, medidorSlotIndex: Int, cultivoId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is SlotDetailUiState.Success) {
                    currentState.copy(isAssigningCultivo = true)
                } else currentState
            }
            try {
                modulosRepository.asignarCultivoAMedidorSlot(moduloId, medidorSlotIndex, cultivoId)
                // Recarga los datos del slot para que la pantalla se actualice
                loadSlotDetail(moduloId, medidorSlotIndex)
            } catch (e: Exception) {
                // Maneja el error, podrías mostrarlo en la UI
                _uiState.value = SlotDetailUiState.Error("Error al asignar el cultivo: ${e.message}")
            } finally {
                _uiState.update { currentState ->
                    if (currentState is SlotDetailUiState.Success) {
                        currentState.copy(isAssigningCultivo = false, showCultivoSelectionDialog = false)
                    } else currentState
                }
            }
        }
    }

    // Funciones para mostrar/ocultar el diálogo
    fun showCultivoDialog() {
        loadCultivosDisponibles()
        _uiState.update { currentState ->
            if (currentState is SlotDetailUiState.Success) {
                currentState.copy(showCultivoSelectionDialog = true)
            } else currentState
        }
    }

    fun hideCultivoDialog() {
        _uiState.update { currentState ->
            if (currentState is SlotDetailUiState.Success) {
                currentState.copy(showCultivoSelectionDialog = false)
            } else currentState
        }
    }
}