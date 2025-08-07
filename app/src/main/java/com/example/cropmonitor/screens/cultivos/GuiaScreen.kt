package com.example.cropmonitor.screens.cultivos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.CultivoListDto
import com.example.cropmonitor.data.models.Temporada
import com.example.cropmonitor.data.models.temporadasFiltro
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.CultivosViewModel
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory


// private const val BASE_URL = "https://10.0.2.2:7016/" // Elimina esta línea

@Composable
fun GuiaScreen(
    reloadKey: Int,
    appContainer: AppContainer,
    onCultivoClick: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    onNotificationsClick: () -> Unit // Parámetro añadido
) {
    val cultivosViewModel: CultivosViewModel = viewModel(
        factory = appContainer.modulosViewModelFactory
    )
    val cultivos by cultivosViewModel.cultivos.collectAsState()
    val isLoading by cultivosViewModel.isLoading.collectAsState()
    val error by cultivosViewModel.error.collectAsState()
    val selectedTemporadaId by cultivosViewModel.selectedTemporadaId.collectAsState()

    LaunchedEffect(reloadKey) {
        cultivosViewModel.loadCultivos()
    }

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = "Guía",
                unreadNotificationsCount = 0, // Valor por defecto
                onNotificationsClick = onNotificationsClick, // Parámetro pasado
                onLogoutClick = onLogoutClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Añadir cultivo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros de temporada
            FiltroTemporadas(
                selectedTemporadaId = selectedTemporadaId,
                onTemporadaSelected = { temporadaId ->
                    cultivosViewModel.loadCultivos(temporadaId = temporadaId, searchText = searchText)
                }
            )

            // Barra de búsqueda
            SearchBar(
                searchText = searchText,
                onSearchTextChange = { newText -> searchText = newText },
                onSearch = {
                    cultivosViewModel.loadCultivos(temporadaId = selectedTemporadaId, searchText = searchText)
                }
            )

            // Título de la sección
            Text(
                text = "Cultivos disponibles",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: $error")
                    }
                }
                cultivos.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No se encontraron cultivos.")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(cultivos) { cultivo ->
                            CultivoCard(
                                cultivo = cultivo,
                                onCultivoClick = { onCultivoClick(cultivo.cultivoID) },
                                onToggleFavorito = {
                                    cultivosViewModel.toggleFavorito(cultivo.cultivoID)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// ... Los composables auxiliares son los mismos
@Composable
fun FiltroTemporadas(
    selectedTemporadaId: Int?,
    onTemporadaSelected: (Int?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedTemporadaId == null,
                onClick = { onTemporadaSelected(null) },
                label = { Text("Todos") }
            )
        }
        items(temporadasFiltro) { temporada ->
            FilterChip(
                selected = temporada.id == selectedTemporadaId,
                onClick = { onTemporadaSelected(temporada.id) },
                label = { Text(text = "${temporada.icono} ${temporada.nombre}") }
            )
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Buscar cultivos...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CultivoCard(cultivo: CultivoListDto, onCultivoClick: () -> Unit, onToggleFavorito: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        onClick = onCultivoClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cargar la imagen del cultivo
            val imageUrl = cultivo.imagenURL ?: ""
            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen de ${cultivo.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = cultivo.nombre, style = MaterialTheme.typography.titleLarge)
                    Text(text = cultivo.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
                }
                Row {
                    IconButton(onClick = { /* TODO: Implementar lógica de cuidados */ }) {
                        Icon(Icons.Default.Spa, contentDescription = "Cuidados")
                    }
                    IconButton(onClick = onToggleFavorito) {
                        Icon(
                            imageVector = if (cultivo.esFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Marcar como favorito",
                            tint = if (cultivo.esFavorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
