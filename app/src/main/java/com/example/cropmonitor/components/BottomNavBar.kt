package com.example.cropmonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Modulos : Screen("modulos", Icons.Default.Home, "Módulos")
    object Guia : Screen("guia", Icons.Default.MenuBook, "Guía")
    object Recetas : Screen("recetas", Icons.Default.Restaurant, "Recetas")
    object Favoritos : Screen("favoritos", Icons.Default.Favorite, "Favoritos")
    object Ajustes : Screen("ajustes", Icons.Default.Settings, "Ajustes")
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemClick: (Screen) -> Unit
) {
    val items = listOf(
        Screen.Modulos,
        Screen.Guia,
        Screen.Recetas,
        Screen.Favoritos,
        Screen.Ajustes
    )

    Row(
        modifier = Modifier.fillMaxWidth().height(70.dp).background(Color(0xFF2E6B4F)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onItemClick(screen) }
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.label,
                    tint = if (isSelected) Color.White else Color.LightGray,
                    modifier = Modifier.size(30.dp)
                )
                if (isSelected) {
                    Text(
                        text = screen.label,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}