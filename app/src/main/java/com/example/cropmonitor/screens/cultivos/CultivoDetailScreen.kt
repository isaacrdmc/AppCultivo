package com.example.cropmonitor.screens.cultivos

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.CultivoDetailDto
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.CultivoDetailUiState
import com.example.cropmonitor.viewmodels.CultivoDetailViewModel
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory

// Esta constante ya no es necesaria, ya que las URLs de las imágenes son completas.
// private const val BASE_URL = "http://10.0.2.2:7016"

@Composable
fun CultivoDetailScreen(
    appContainer: AppContainer,
    onBackClick: () -> Unit
) {
    val viewModel: CultivoDetailViewModel = viewModel(
        factory = appContainer.modulosViewModelFactory
    )

    val uiState by viewModel.uiState.collectAsState()

    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalles del Cultivo") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is CultivoDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is CultivoDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is CultivoDetailUiState.Success -> {
                CultivoDetailContent(cultivo = state.cultivo, modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

@Composable
fun CultivoDetailContent(cultivo: CultivoDetailDto, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Nombre del cultivo
        Text(text = cultivo.nombre, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Imagen del cultivo
        // Simplemente usa la URL directamente, ya que es completa.
        val imageUrl = cultivo.imagenURL ?: ""

        AsyncImage(
            model = imageUrl,
            contentDescription = "Imagen de ${cultivo.nombre}",
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del cultivo
        Text(text = "Descripción", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = cultivo.descripcion ?: "Sin descripción.", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Requisitos de cultivo
        Text(text = "Requisitos de Cultivo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        RequisitosCard(
            clima = cultivo.requisitosClima,
            agua = cultivo.requisitosAgua,
            luz = cultivo.requisitosLuz
        )
    }
}

@Composable
fun RequisitosCard(clima: String?, agua: String?, luz: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            RequisitoItem(emoji = "🌡️", nombre = "Clima", descripcion = clima ?: "No especificado")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            RequisitoItem(emoji = "💧", nombre = "Agua", descripcion = agua ?: "No especificado")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            RequisitoItem(emoji = "☀️", nombre = "Luz", descripcion = luz ?: "No especificado")
        }
    }
}

@Composable
fun RequisitoItem(emoji: String, nombre: String, descripcion: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = emoji, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = nombre, fontWeight = FontWeight.Bold)
            Text(text = descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}