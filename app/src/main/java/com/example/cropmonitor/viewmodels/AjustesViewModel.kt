package com.example.cropmonitor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cropmonitor.data.models.ChangePasswordDto
import com.example.cropmonitor.data.models.UsuarioProfileDto
import com.example.cropmonitor.data.repository.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface AjustesUiState {
    data object Loading : AjustesUiState
    data class Success(val profile: UsuarioProfileDto) : AjustesUiState
    data class Error(val message: String) : AjustesUiState
}

// Estados para las acciones específicas
sealed interface PasswordChangeState {
    data object Idle : PasswordChangeState
    data object Loading : PasswordChangeState
    data object Success : PasswordChangeState
    data class Error(val message: String) : PasswordChangeState
}

sealed interface DeleteAccountState {
    data object Idle : DeleteAccountState
    data object Loading : DeleteAccountState
    data object Success : DeleteAccountState
    data class Error(val message: String) : DeleteAccountState
}

class AjustesViewModel(
    private val usuariosRepository: UsuariosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AjustesUiState>(AjustesUiState.Loading)
    val uiState: StateFlow<AjustesUiState> = _uiState

    // Nuevos estados para las acciones
    private val _passwordChangeState = MutableStateFlow<PasswordChangeState>(PasswordChangeState.Idle)
    val passwordChangeState: StateFlow<PasswordChangeState> = _passwordChangeState

    private val _deleteAccountState = MutableStateFlow<DeleteAccountState>(DeleteAccountState.Idle)
    val deleteAccountState: StateFlow<DeleteAccountState> = _deleteAccountState

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            _uiState.value = AjustesUiState.Loading
            val result = usuariosRepository.getUserProfile()
            result.onSuccess { profile ->
                _uiState.value = AjustesUiState.Success(profile)
            }.onFailure {
                _uiState.value = AjustesUiState.Error("Error al cargar perfil: ${it.message}")
            }
        }
    }

    // Lógica para cambiar la contraseña
    fun changePassword(dto: ChangePasswordDto) {
        viewModelScope.launch {
            _passwordChangeState.value = PasswordChangeState.Loading
            val result = usuariosRepository.changePassword(dto)
            result.onSuccess {
                _passwordChangeState.value = PasswordChangeState.Success
            }.onFailure {
                _passwordChangeState.value = PasswordChangeState.Error("Error: ${it.message}")
            }
        }
    }

    // Lógica para eliminar la cuenta
    fun deleteAccount() {
        viewModelScope.launch {
            _deleteAccountState.value = DeleteAccountState.Loading
            val result = usuariosRepository.deleteAccount()
            result.onSuccess {
                _deleteAccountState.value = DeleteAccountState.Success
            }.onFailure {
                _deleteAccountState.value = DeleteAccountState.Error("Error al eliminar la cuenta: ${it.message}")
            }
        }
    }

    // Método para reiniciar los estados
    fun resetPasswordState() {
        _passwordChangeState.value = PasswordChangeState.Idle
    }

    fun resetDeleteAccountState() {
        _deleteAccountState.value = DeleteAccountState.Idle
    }
}