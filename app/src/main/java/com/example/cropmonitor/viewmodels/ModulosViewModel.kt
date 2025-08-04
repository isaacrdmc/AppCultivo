package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.models.ModuloCreateDto
import com.example.cropmonitor.data.models.ModuloListDto
import com.example.cropmonitor.data.repository.ModulosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ModulosViewModel(private val repository: ModulosRepository) : ViewModel() {

    private val _modulos = MutableStateFlow<List<ModuloListDto>>(emptyList())
    val modulos: StateFlow<List<ModuloListDto>> = _modulos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadModulos()
    }

    fun loadModulos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Se llama al método con el nombre correcto
                val response: Response<List<ModuloListDto>> = repository.getModulosUsuario()
                if (response.isSuccessful) {
                    // Se verifica que la respuesta sea exitosa y se extrae el cuerpo
                    _modulos.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar los módulos: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar los módulos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createModulo(modulo: ModuloCreateDto) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response: Response<ModuloListDto> = repository.createModulo(modulo)
                if (response.isSuccessful) {
                    val newModulo = response.body()
                    if (newModulo != null) {
                        _modulos.value = _modulos.value + newModulo
                    } else {
                        _error.value = "Error: El módulo creado no tiene datos"
                    }
                } else {
                    _error.value = "Error al crear el módulo: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error al crear el módulo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}