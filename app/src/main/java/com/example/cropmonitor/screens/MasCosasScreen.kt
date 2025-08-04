package com.example.cropmonitor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cropmonitor.ui.components.TopBar
import androidx.compose.material3.Scaffold

@Composable
fun MasCosasScreen() {
    Scaffold(
        topBar = {
            TopBar(
                title = "Más",
                onLogoutClick = { /* No hay acción de logout aquí, se maneja en MainActivity */ }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8EDEE))
                .padding(padding)
        ) {
            Text(
                text = "Si quiere una mejor experiencia y mayores facilidades para monitorear tus cultivos visita nuestro sitio para poder obtener uno de los modulos que tenemos disponibles:",
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}