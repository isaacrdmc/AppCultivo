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
import com.example.cropmonitor.data.models.RecetaListDto
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.RecetasUiState
import com.example.cropmonitor.viewmodels.RecetasViewModel

@Composable
fun RecetasScreen(
    appContainer: AppContainer,
    onRecetaClick: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    val viewModel: RecetasViewModel = viewModel(factory = appContainer.recetasViewModelFactory)
    val uiState by viewModel.recetasUiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Recetas de Cultivos",
                onLogoutClick = onLogoutClick
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is RecetasUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is RecetasUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                is RecetasUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.recetas) { receta ->
                            RecetaCard(receta = receta, onClick = { onRecetaClick(receta.recetaID) })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetaCard(receta: RecetaListDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = receta.nombreReceta, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = receta.descripcionCorta ?: "Sin descripción", style = MaterialTheme.typography.bodyMedium)
        }
    }
}