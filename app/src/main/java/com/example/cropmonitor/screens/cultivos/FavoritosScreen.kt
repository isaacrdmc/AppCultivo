package com.example.cropmonitor.screens.cultivos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.CultivoListDto
import com.example.cropmonitor.data.models.Temporada
import com.example.cropmonitor.data.models.temporadasFiltro
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.CultivoDetailViewModel
import com.example.cropmonitor.viewmodels.FavoritosUiState
import com.example.cropmonitor.viewmodels.FavoritosViewModel
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory

private const val BASE_URL = "https://10.0.2.2:7016/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    appContainer: AppContainer,
    onCultivoClick: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    val viewModel: FavoritosViewModel = viewModel(
        factory = appContainer.modulosViewModelFactory
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Favoritos",
                onLogoutClick = onLogoutClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Tus cultivos favoritos:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            when (val state = uiState) {
                is FavoritosUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is FavoritosUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is FavoritosUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        temporadasFiltro.forEach { temporada ->
                            val cultivosPorTemporada = state.cultivos.filter { cultivo ->
                                cultivo.temporadas.contains(temporada.nombre)
                            }
                            if (cultivosPorTemporada.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "${temporada.icono} ${temporada.nombre}",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                items(cultivosPorTemporada) { cultivo ->
                                    FavoritoCard(
                                        cultivo = cultivo,
                                        temporadaColor = temporada.color,
                                        onCultivoClick = { onCultivoClick(cultivo.cultivoID) }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritoCard(cultivo: CultivoListDto, temporadaColor: Color, onCultivoClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onCultivoClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(temporadaColor) // Color de fondo según la estación
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = if (cultivo.imagenURL != null) BASE_URL + cultivo.imagenURL.removePrefix("/") else ""
            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen de ${cultivo.nombre}",
                modifier = Modifier.size(64.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cultivo.nombre, style = MaterialTheme.typography.titleLarge)
                Text(text = cultivo.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Spa, contentDescription = "Cuidados", tint = MaterialTheme.colorScheme.onBackground)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorito", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}