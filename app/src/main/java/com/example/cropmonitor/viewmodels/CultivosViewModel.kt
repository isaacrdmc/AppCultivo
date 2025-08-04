package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.models.CultivoListDto
import com.example.cropmonitor.data.repository.CultivosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CultivosViewModel(
    private val repository: CultivosRepository
) : ViewModel() {

    private val _cultivos = MutableStateFlow<List<CultivoListDto>>(emptyList())
    val cultivos: StateFlow<List<CultivoListDto>> = _cultivos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado del filtro de temporada
    private val _selectedTemporadaId = MutableStateFlow<Int?>(null)
    val selectedTemporadaId: StateFlow<Int?> = _selectedTemporadaId

    init {
        loadCultivos()
    }

    fun loadCultivos(temporadaId: Int? = null, searchText: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedTemporadaId.value = temporadaId

            val result = repository.getCultivos(temporadaId, searchText)
            result.onSuccess {
                _cultivos.value = it
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun toggleFavorito(cultivoId: Int) {
        viewModelScope.launch {
            repository.toggleFavorito(cultivoId)
            // Después de cambiar el estado, recargamos la lista para reflejar el cambio
            loadCultivos(selectedTemporadaId.value)
        }
    }
}