package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.repository.RecetasRepository
import com.example.cropmonitor.data.models.RecetaListDto
import com.example.cropmonitor.data.models.RecetaDetailDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface RecetasUiState {
    object Loading : RecetasUiState
    data class Success(val recetas: List<RecetaListDto>) : RecetasUiState
    data class Error(val message: String) : RecetasUiState
}

sealed interface RecetaDetailUiState {
    object Loading : RecetaDetailUiState
    data class Success(val receta: RecetaDetailDto) : RecetaDetailUiState
    data class Error(val message: String) : RecetaDetailUiState
}

class RecetasViewModel(private val recetasRepository: RecetasRepository) : ViewModel() {

    private val _recetasUiState = MutableStateFlow<RecetasUiState>(RecetasUiState.Loading)
    val recetasUiState: StateFlow<RecetasUiState> = _recetasUiState.asStateFlow()

    private val _recetaDetailUiState = MutableStateFlow<RecetaDetailUiState>(RecetaDetailUiState.Loading)
    val recetaDetailUiState: StateFlow<RecetaDetailUiState> = _recetaDetailUiState.asStateFlow()

    init {
        loadRecetas()
    }

    fun loadRecetas() {
        viewModelScope.launch {
            _recetasUiState.value = RecetasUiState.Loading
            try {
                val recetas = recetasRepository.getRecetas()
                _recetasUiState.value = RecetasUiState.Success(recetas)
            } catch (e: IOException) {
                _recetasUiState.value = RecetasUiState.Error("Error de red: ${e.message}")
            } catch (e: Exception) {
                _recetasUiState.value = RecetasUiState.Error("Error al cargar las recetas: ${e.message}")
            }
        }
    }

    fun loadReceta(recetaId: Int) {
        viewModelScope.launch {
            _recetaDetailUiState.value = RecetaDetailUiState.Loading
            try {
                val receta = recetasRepository.getReceta(recetaId)
                _recetaDetailUiState.value = RecetaDetailUiState.Success(receta)
            } catch (e: IOException) {
                _recetaDetailUiState.value = RecetaDetailUiState.Error("Error de red: ${e.message}")
            } catch (e: Exception) {
                _recetaDetailUiState.value = RecetaDetailUiState.Error("Error al cargar la receta: ${e.message}")
            }
        }
    }

    fun searchRecetas(query: String) {
        if (query.isEmpty()) {
            loadRecetas()
            return
        }
        viewModelScope.launch {
            _recetasUiState.value = RecetasUiState.Loading
            try {
                val recetas = recetasRepository.searchRecetas(query)
                _recetasUiState.value = RecetasUiState.Success(recetas)
            } catch (e: IOException) {
                _recetasUiState.value = RecetasUiState.Error("Error de red: ${e.message}")
            } catch (e: Exception) {
                _recetasUiState.value = RecetasUiState.Error("Error al buscar recetas: ${e.message}")
            }
        }
    }
}

// Factoría para crear el ViewModel con el repositorio
class RecetasViewModelFactory(private val recetasRepository: RecetasRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecetasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecetasViewModel(recetasRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}