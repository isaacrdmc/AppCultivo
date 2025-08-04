package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.models.CultivoListDto
import com.example.cropmonitor.data.repository.CultivosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface FavoritosUiState {
    data object Loading : FavoritosUiState
    data class Success(val cultivos: List<CultivoListDto>) : FavoritosUiState
    data class Error(val message: String) : FavoritosUiState
}

class FavoritosViewModel(
    private val cultivosRepository: CultivosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritosUiState>(FavoritosUiState.Loading)
    val uiState: StateFlow<FavoritosUiState> = _uiState

    init {
        loadFavoriteCultivos()
    }

    fun loadFavoriteCultivos() {
        viewModelScope.launch {
            _uiState.value = FavoritosUiState.Loading
            val result = cultivosRepository.getFavoriteCultivos()
            result.onSuccess { cultivos ->
                _uiState.value = FavoritosUiState.Success(cultivos)
            }.onFailure {
                _uiState.value = FavoritosUiState.Error("Error al cargar favoritos: ${it.message}")
            }
        }
    }
}