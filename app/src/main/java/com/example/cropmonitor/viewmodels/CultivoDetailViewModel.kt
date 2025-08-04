package com.example.cropmonitor.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.models.CultivoDetailDto
import com.example.cropmonitor.data.repository.CultivosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estado de la UI para la pantalla de detalle
sealed interface CultivoDetailUiState {
    data object Loading : CultivoDetailUiState
    data class Success(val cultivo: CultivoDetailDto) : CultivoDetailUiState
    data class Error(val message: String) : CultivoDetailUiState
}

class CultivoDetailViewModel(
    private val repository: CultivosRepository,
    private val savedStateHandle: SavedStateHandle // Permite recuperar el estado al recrear el ViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow<CultivoDetailUiState>(CultivoDetailUiState.Loading)
    val uiState: StateFlow<CultivoDetailUiState> = _uiState

    init {
        // Obtenemos el cultivoId de los argumentos de navegación
        val cultivoId: Int? = savedStateHandle["cultivoId"]
        if (cultivoId != null) {
            loadCultivoDetail(cultivoId)
        } else {
            _uiState.value = CultivoDetailUiState.Error("ID de cultivo no proporcionado.")
        }
    }

    private fun loadCultivoDetail(cultivoId: Int) {
        viewModelScope.launch {
            _uiState.value = CultivoDetailUiState.Loading
            val result = repository.getCultivoById(cultivoId)
            result.onSuccess { cultivo ->
                _uiState.value = CultivoDetailUiState.Success(cultivo)
            }.onFailure {
                _uiState.value = CultivoDetailUiState.Error("Error al cargar los detalles: ${it.message}")
            }
        }
    }
}