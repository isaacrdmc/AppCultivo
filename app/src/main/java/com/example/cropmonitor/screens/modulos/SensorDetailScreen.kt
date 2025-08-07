package com.example.cropmonitor.screens.modulos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.SensorLecturaDto
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.SlotDetailUiState
import com.example.cropmonitor.viewmodels.SlotDetailViewModel

@Composable
fun SensorDetailScreen(
    moduloId: Int,
    medidorSlotIndex: Int,
    appContainer: AppContainer,
    onBackClick: () -> Unit, // Parámetro añadido para el botón de retroceso
    onNotificationsClick: () -> Unit // Parámetro añadido
) {
    val viewModel: SlotDetailViewModel = viewModel(factory = appContainer.modulosViewModelFactory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(moduloId, medidorSlotIndex) {
        viewModel.loadSlotDetail(moduloId, medidorSlotIndex)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Slot $medidorSlotIndex",
                unreadNotificationsCount = 0, // Valor por defecto
                onNotificationsClick = onNotificationsClick, // Parámetro pasado
                onLogoutClick = { onBackClick() } // Usamos onBackClick para el botón de logout
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is SlotDetailUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is SlotDetailUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                is SlotDetailUiState.Success -> {
                    val medidorSlot = state.medidorSlot

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        if (medidorSlot.enUso) {
                            // --- Contenido para un slot con cultivo ---
                            val fullImageUrl = "https://10.0.2.2:7016${medidorSlot.cultivoImagenURL}"
                            AsyncImage(
                                model = fullImageUrl,
                                contentDescription = "Imagen de ${medidorSlot.cultivoNombre}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cultivo: ${medidorSlot.cultivoNombre ?: "N/A"}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(medidorSlot.sensores) { sensor ->
                                    SensorCard(sensor = sensor)
                                }
                            }
                        } else {
                            // --- Contenido para un slot vacío ---
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Button(
                                    onClick = { viewModel.showCultivoDialog() },
                                    modifier = Modifier.fillMaxWidth(0.6f)
                                ) {
                                    Text("Agregar Cultivo")
                                }
                            }
                        }
                    }

                    // --- Diálogo para seleccionar el cultivo (fuera de la columna) ---
                    if (state.showCultivoSelectionDialog) {
                        AlertDialog(
                            onDismissRequest = { viewModel.hideCultivoDialog() },
                            title = { Text("Seleccionar Cultivo") },
                            text = {
                                if (state.cultivosDisponibles.isEmpty() && !state.isAssigningCultivo) {
                                    Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                                        items(state.cultivosDisponibles) { cultivo ->
                                            TextButton(
                                                onClick = {
                                                    viewModel.asignarCultivo(moduloId, medidorSlotIndex, cultivo.cultivoID)
                                                }
                                            ) {
                                                Text(text = cultivo.nombre, style = MaterialTheme.typography.bodyLarge)
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { viewModel.hideCultivoDialog() }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SensorCard(sensor: SensorLecturaDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Tipo de Sensor: ${sensor.tipoSensor}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Valor: ${sensor.valorLectura ?: "N/A"} ${sensor.unidadMedida}", style = MaterialTheme.typography.bodyLarge)
            sensor.estadoRiego?.let {
                Text(text = "Estado de Riego: $it", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
