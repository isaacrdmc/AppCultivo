package com.example.cropmonitor.screens.modulos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.MedidorSlotDto
import com.example.cropmonitor.viewmodels.SensoresViewModel
import com.example.cropmonitor.ui.components.TopBar

@Composable
fun SensoresScreen(
    moduloId: Int,
    onCultivoSlotClick: (medidorSlotIndex: Int) -> Unit, // <-- Usamos solo esta lambda
    appContainer: AppContainer
) {
    val viewModel: SensoresViewModel = viewModel(factory = appContainer.modulosViewModelFactory)
    val medidores by viewModel.medidores.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(moduloId) {
        viewModel.loadSensores(moduloId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Sensores del Módulo",
                onLogoutClick = { /* No se implementa aquí */ }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error)
                    }
                }
                medidores.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay medidores en este módulo.")
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(medidores) { medidorSlotDto ->
                            // CORREGIDO: Pasa el DTO y la lambda correcta
                            MedidorSlotCard(
                                medidorSlot = medidorSlotDto,
                                onCultivoSlotClick = onCultivoSlotClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedidorSlotCard(medidorSlot: MedidorSlotDto, onCultivoSlotClick: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // CORREGIDO: Llama a la nueva lambda con el medidorSlotIndex
        onClick = {
            onCultivoSlotClick(medidorSlot.medidorSlotIndex)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Slot ${medidorSlot.medidorSlotIndex}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            val slotText = if (medidorSlot.enUso) medidorSlot.cultivoNombre ?: "Cultivo Sin Nombre" else "Espacio Vacío"
            Text(text = slotText, style = MaterialTheme.typography.bodyMedium)

            medidorSlot.sensores.forEach { sensor ->
                Text(
                    text = "${sensor.tipoSensor}: ${sensor.valorLectura ?: "N/A"} ${sensor.unidadMedida}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}