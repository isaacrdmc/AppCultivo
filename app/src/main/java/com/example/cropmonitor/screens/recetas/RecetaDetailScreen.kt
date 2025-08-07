package com.example.cropmonitor.screens.recetas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.RecetaDetailUiState
import com.example.cropmonitor.viewmodels.RecetasViewModel

@Composable
fun RecetaDetailScreen(
    appContainer: AppContainer,
    recetaId: Int,
    onBackClick: () -> Unit,
    onNotificationsClick: () -> Unit // Parámetro añadido
) {
    val viewModel: RecetasViewModel = viewModel(factory = appContainer.recetasViewModelFactory)
    val uiState by viewModel.recetaDetailUiState.collectAsState()

    LaunchedEffect(recetaId) {
        viewModel.loadReceta(recetaId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Detalle de Receta",
                unreadNotificationsCount = 0, // Valor por defecto
                onNotificationsClick = onNotificationsClick, // Parámetro pasado
                onLogoutClick = onBackClick // Usamos onBackClick para el botón de logout
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is RecetaDetailUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is RecetaDetailUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                is RecetaDetailUiState.Success -> {
                    val receta = state.receta
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(text = receta.NombreReceta, style = MaterialTheme.typography.headlineLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = receta.Descripcion ?: "Sin descripción.", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Instrucciones", style = MaterialTheme.typography.headlineSmall)
                            Text(text = receta.Instrucciones ?: "Sin instrucciones.", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Cultivos Necesarios", style = MaterialTheme.typography.headlineSmall)
                        }
                        items(receta.CultivosNecesarios) { cultivo ->
                            Text(text = "• ${cultivo.NombreCultivo}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
