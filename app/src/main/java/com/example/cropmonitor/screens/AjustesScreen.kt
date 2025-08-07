package com.example.cropmonitor.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.ChangePasswordDto
import com.example.cropmonitor.data.models.UsuarioProfileDto
import com.example.cropmonitor.R
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.AjustesUiState
import com.example.cropmonitor.viewmodels.AjustesViewModel
import com.example.cropmonitor.viewmodels.DeleteAccountState
import com.example.cropmonitor.viewmodels.PasswordChangeState

@Composable
fun AjustesScreen(
    reloadKey: Int,
    appContainer: AppContainer,
    onLogoutClick: () -> Unit,
    onNotificacionConfigClick: () -> Unit // Nuevo parámetro para la navegación a la configuración de notificaciones
) {
    // Se crea la fábrica de ViewModels para obtener el AjustesViewModel
    val viewModel: AjustesViewModel = viewModel(
        factory = appContainer.modulosViewModelFactory
    )
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(reloadKey) {
        viewModel.fetchUserProfile()
    }

    // Estado para manejar el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopBar(
                title = "Configuración",
                onLogoutClick = onLogoutClick,
                onNotificationsClick = onNotificacionConfigClick, // Added the missing parameter
                unreadNotificationsCount = 0 // Added the missing parameter with a default value
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (val state = uiState) {
            is AjustesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AjustesUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is AjustesUiState.Success -> {
                AjustesContent(
                    profile = state.profile,
                    onLogoutClick = onLogoutClick,
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(paddingValues),
                    onNotificacionConfigClick = onNotificacionConfigClick // Pasamos el nuevo callback
                )
            }
        }
    }
}

@Composable
fun AjustesContent(
    profile: UsuarioProfileDto,
    onLogoutClick: () -> Unit,
    viewModel: AjustesViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onNotificacionConfigClick: () -> Unit // Nuevo parámetro para la navegación
) {
    // Estados para los campos de contraseña
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    // Estado para el diálogo de confirmación de eliminar cuenta
    var showDeleteDialog by remember { mutableStateOf(false) }

    val passwordChangeState by viewModel.passwordChangeState.collectAsState()
    val deleteAccountState by viewModel.deleteAccountState.collectAsState()

    // LaunchedEffect para manejar el feedback del cambio de contraseña
    LaunchedEffect(passwordChangeState) {
        when (val state = passwordChangeState) {
            is PasswordChangeState.Success -> {
                snackbarHostState.showSnackbar("Contraseña cambiada exitosamente.")
                currentPassword = ""
                newPassword = ""
                confirmNewPassword = ""
                viewModel.resetPasswordState()
            }
            is PasswordChangeState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetPasswordState()
            }
            else -> {}
        }
    }

    // LaunchedEffect para manejar el feedback de la eliminación de cuenta
    LaunchedEffect(deleteAccountState) {
        when (deleteAccountState) {
            is DeleteAccountState.Success -> {
                onLogoutClick() // Forzamos el logout y la navegación
                viewModel.resetDeleteAccountState()
            }
            is DeleteAccountState.Error -> {
                snackbarHostState.showSnackbar("Error al eliminar la cuenta.")
                viewModel.resetDeleteAccountState()
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Perfil de Usuario ---
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.module_icon),
                contentDescription = "Imagen de perfil",
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = profile.nombreUsuario,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = profile.correo,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // --- Opción para Notificaciones (NUEVA FUNCIONALIDAD) ---
        ListItem(
            headlineContent = { Text("Configuración de notificaciones") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Configuración de notificaciones"
                )
            },
            modifier = Modifier.fillMaxWidth().clickable { onNotificacionConfigClick() }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // --- Sección de Contraseña ---
        Text(
            text = "Cambiar Contraseña",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Contraseña Actual") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar/Ocultar contraseña"
                    )
                }
            }
        )
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = confirmNewPassword,
            onValueChange = { confirmNewPassword = it },
            label = { Text("Confirmar Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                val dto = ChangePasswordDto(currentPassword, newPassword, confirmNewPassword)
                viewModel.changePassword(dto)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = passwordChangeState !is PasswordChangeState.Loading
        ) {
            if (passwordChangeState is PasswordChangeState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Cambiar Contraseña")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // --- Inicio de sesión con Google ---
        Text(
            text = "Inicio de sesión con Google",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = "Esta funcionalidad no está activa.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // --- Eliminar Cuenta ---
        Button(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            enabled = deleteAccountState !is DeleteAccountState.Loading
        ) {
            if (deleteAccountState is DeleteAccountState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Eliminar cuenta")
            }
        }

        // --- Diálogo de confirmación ---
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("¿Estás seguro?") },
                text = { Text("Esta acción es irreversible y eliminará todos tus datos. ¿Deseas continuar?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Pie de página ---
        Text(
            text = "Versión 1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
