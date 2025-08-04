package com.example.cropmonitor.screens.modulos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.data.models.ModuloListDto
import com.example.cropmonitor.ui.components.TopBar
import com.example.cropmonitor.viewmodels.ModulosViewModel
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory

@Composable
fun ModulosScreen(
    reloadKey: Int,
    onModuloClick: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    appContainer: AppContainer
) {
    val viewModel: ModulosViewModel = viewModel(factory = appContainer.modulosViewModelFactory)
    val modulos by viewModel.modulos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    LaunchedEffect(reloadKey) {
        viewModel.loadModulos()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Módulos",
                onLogoutClick = onLogoutClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Lógica para crear un nuevo módulo */ }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir módulo")
            }
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
                modulos.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No tienes módulos. ¡Crea uno!")
                    }
                }
                else -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(modulos) { modulo ->
                            ModuloItem(modulo = modulo, onClick = { onModuloClick(modulo.moduloID) })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuloItem(modulo: ModuloListDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = modulo.nombreModulo, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estado: ${modulo.estado}")
            Text(text = "Cultivos: ${modulo.cantidadCultivosActual}/${modulo.cantidadCultivosMax}")
        }
    }
}