package com.example.cropmonitor.screens.notificaciones

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.UpdateConfiguracionDto
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory
import com.example.cropmonitor.viewmodels.NotificacionesUiState
import com.example.cropmonitor.viewmodels.NotificacionesViewModel

// Importamos Calendar para compatibilidad con API 21
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionConfiguracionScreen(
    appContainer: AppContainer,
    onBackClick: () -> Unit
) {
    val viewModel: NotificacionesViewModel = viewModel(
        factory = appContainer.modulosViewModelFactory
    )
    val configuracionUiState by viewModel.configuracionUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadConfiguracion()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = configuracionUiState) {
            is NotificacionesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is NotificacionesUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Error al cargar la configuración.")
                }
            }
            is NotificacionesUiState.SuccessConfiguracion -> {
                val configuracion = state.configuracion
                var frecuenciaRiego by remember { mutableStateOf(configuracion.frecuenciaRiego ?: "Diario") }

                // Usamos enteros para hora y minuto para evitar LocalTime
                var horarioNotificacionHour by remember { mutableStateOf(8) }
                var horarioNotificacionMinute by remember { mutableStateOf(0) }

                var activarRiegoAutomatico by remember { mutableStateOf(configuracion.activarRiegoAutomatico) }
                var tipoAlertaSensor by remember { mutableStateOf(configuracion.tipoAlertaSensor ?: "Notificación en app") }
                var habilitarRecomendaciones by remember { mutableStateOf(configuracion.habilitarRecomendacionesEstacionales) }

                val showTimePicker = remember { mutableStateOf(false) }

                LaunchedEffect(configuracion) {
                    frecuenciaRiego = configuracion.frecuenciaRiego ?: "Diario"
                    activarRiegoAutomatico = configuracion.activarRiegoAutomatico
                    tipoAlertaSensor = configuracion.tipoAlertaSensor ?: "Notificación en app"
                    habilitarRecomendaciones = configuracion.habilitarRecomendacionesEstacionales

                    // Parseamos el string de hora a enteros
                    val timeString = configuracion.horarioNotificacion ?: "08:00"
                    val parts = timeString.split(":")
                    if (parts.size == 2) {
                        horarioNotificacionHour = parts[0].toIntOrNull() ?: 8
                        horarioNotificacionMinute = parts[1].toIntOrNull() ?: 0
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Frecuencia de Riego
                    Text("Frecuencia de Riego", style = MaterialTheme.typography.titleMedium)
                    DropdownMenuSelector(
                        options = listOf("Diario", "Semanal"),
                        selectedOption = frecuenciaRiego,
                        onOptionSelected = {
                            frecuenciaRiego = it
                            val newTime = String.format("%02d:%02d", horarioNotificacionHour, horarioNotificacionMinute)
                            viewModel.updateConfiguracion(
                                UpdateConfiguracionDto(
                                    frecuenciaRiego = frecuenciaRiego,
                                    horarioNotificacion = newTime,
                                    activarRiegoAutomatico = activarRiegoAutomatico,
                                    tipoAlertaSensor = tipoAlertaSensor,
                                    habilitarRecomendacionesEstacionales = habilitarRecomendaciones
                                )
                            )
                        }
                    )

                    // Horario de Notificación
                    Text("Horario de Notificación", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = { showTimePicker.value = true }) {
                        Text("Seleccionar hora: ${String.format("%02d:%02d", horarioNotificacionHour, horarioNotificacionMinute)}")
                    }
                    if (showTimePicker.value) {
                        TimePickerDialog(
                            onDismissRequest = { showTimePicker.value = false },
                            onTimeSelected = { newHour, newMinute ->
                                horarioNotificacionHour = newHour
                                horarioNotificacionMinute = newMinute
                                showTimePicker.value = false
                                val newTime = String.format("%02d:%02d", newHour, newMinute)
                                viewModel.updateConfiguracion(
                                    UpdateConfiguracionDto(
                                        frecuenciaRiego = frecuenciaRiego,
                                        horarioNotificacion = newTime,
                                        activarRiegoAutomatico = activarRiegoAutomatico,
                                        tipoAlertaSensor = tipoAlertaSensor,
                                        habilitarRecomendacionesEstacionales = habilitarRecomendaciones
                                    )
                                )
                            },
                            initialHour = horarioNotificacionHour,
                            initialMinute = horarioNotificacionMinute
                        )
                    }

                    // Tipo de Alerta del Sensor
                    Text("Tipo de Alerta del Sensor", style = MaterialTheme.typography.titleMedium)
                    DropdownMenuSelector(
                        options = listOf("Email", "Notificación en app"),
                        selectedOption = tipoAlertaSensor,
                        onOptionSelected = {
                            tipoAlertaSensor = it
                            val newTime = String.format("%02d:%02d", horarioNotificacionHour, horarioNotificacionMinute)
                            viewModel.updateConfiguracion(
                                UpdateConfiguracionDto(
                                    frecuenciaRiego = frecuenciaRiego,
                                    horarioNotificacion = newTime,
                                    activarRiegoAutomatico = activarRiegoAutomatico,
                                    tipoAlertaSensor = tipoAlertaSensor,
                                    habilitarRecomendacionesEstacionales = habilitarRecomendaciones
                                )
                            )
                        }
                    )

                    // Activar Riego Automático
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Activar Riego Automático", style = MaterialTheme.typography.titleMedium)
                        Switch(
                            checked = activarRiegoAutomatico,
                            onCheckedChange = {
                                activarRiegoAutomatico = it
                                val newTime = String.format("%02d:%02d", horarioNotificacionHour, horarioNotificacionMinute)
                                viewModel.updateConfiguracion(
                                    UpdateConfiguracionDto(
                                        frecuenciaRiego = frecuenciaRiego,
                                        horarioNotificacion = newTime,
                                        activarRiegoAutomatico = activarRiegoAutomatico,
                                        tipoAlertaSensor = tipoAlertaSensor,
                                        habilitarRecomendacionesEstacionales = habilitarRecomendaciones
                                    )
                                )
                            }
                        )
                    }

                    // Habilitar Recomendaciones Estacionales
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Habilitar Recomendaciones", style = MaterialTheme.typography.titleMedium)
                        Switch(
                            checked = habilitarRecomendaciones,
                            onCheckedChange = {
                                habilitarRecomendaciones = it
                                val newTime = String.format("%02d:%02d", horarioNotificacionHour, horarioNotificacionMinute)
                                viewModel.updateConfiguracion(
                                    UpdateConfiguracionDto(
                                        frecuenciaRiego = frecuenciaRiego,
                                        horarioNotificacion = newTime,
                                        activarRiegoAutomatico = activarRiegoAutomatico,
                                        tipoAlertaSensor = tipoAlertaSensor,
                                        habilitarRecomendacionesEstacionales = habilitarRecomendaciones
                                    )
                                )
                            }
                        )
                    }
                }
            }
            is NotificacionesUiState.Success -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Configuración no disponible.")
                }
            }
        }
    }
}

// Componente reutilizable para el menú desplegable
@Composable
fun DropdownMenuSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    initialHour: Int,
    initialMinute: Int
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true // Formato de 24 horas por defecto
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar Hora") },
        text = {
            Column {
                TimePicker(state = timePickerState)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(timePickerState.hour, timePickerState.minute)
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}
