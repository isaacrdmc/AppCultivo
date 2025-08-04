package com.example.cropmonitor.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.cropmonitor.ui.components.TopBar

@Composable
fun EstadisticasScreen() {
    Scaffold(
        topBar = {
            TopBar(
                title = "Estadísticas",
                onLogoutClick = { /* No hay acción de logout aquí, se maneja en MainActivity */ }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Pantalla de Estadísticas", fontSize = 24.sp)
        }
    }
}