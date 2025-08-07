package com.example.cropmonitor.screens.notificaciones

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.NotificacionDto
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory
import com.example.cropmonitor.viewmodels.NotificacionesUiState
import com.example.cropmonitor.viewmodels.NotificacionesViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
// Eliminamos la importación de TopBar, ya que no la necesitamos
// import com.example.cropmonitor.ui.components.TopBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    appContainer: AppContainer,
    onBackClick: () -> Unit,
    onConfiguracionClick: () -> Unit
) {
    val viewModel: NotificacionesViewModel = viewModel(
        factory = appContainer.modulosViewModelFactory
    )
    val uiState by viewModel.notificacionesUiState.collectAsState()

    // Cargar notificaciones al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadNotificaciones()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onConfiguracionClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Configuración")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is NotificacionesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is NotificacionesUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Error al cargar notificaciones.")
                }
            }
            is NotificacionesUiState.Success -> {
                if (state.notificaciones.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                        Text("No tienes notificaciones.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        items(state.notificaciones, key = { it.notificacionID }) { notificacion ->
                            NotificacionItem(
                                notificacion = notificacion,
                                onClick = { viewModel.marcarComoLeida(notificacion.notificacionID) }
                            )
                        }
                    }
                }
            }
            is NotificacionesUiState.SuccessConfiguracion -> {} // No es necesario hacer nada aquí
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificacionItem(notificacion: NotificacionDto, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val isUnread = !notificacion.leida

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = notificacion.tipoNotificacion,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = LocalDateTime.parse(notificacion.fechaHoraEnvio).format(formatter),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = notificacion.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isUnread) FontWeight.SemiBold else FontWeight.Normal
            )
            // Información adicional del cultivo o sensor
            if (notificacion.cultivoNombre != null) {
                Text(
                    text = "Cultivo: ${notificacion.cultivoNombre}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            if (notificacion.sensorNombre != null) {
                Text(
                    text = "Sensor: ${notificacion.sensorNombre}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
