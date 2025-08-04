package com.example.cropmonitor.data.models

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName

// Modelo para la lista de cultivos
data class CultivoListDto(
    @SerializedName("cultivoID") val cultivoID: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("imagenURL") val imagenURL: String?,
    @SerializedName("esFavorito") val esFavorito: Boolean,
    @SerializedName("temporadas") val temporadas: List<String>
)

// Modelo para el detalle de un cultivo
data class CultivoDetailDto(
    @SerializedName("cultivoID") val cultivoID: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("imagenURL") val imagenURL: String?,
    @SerializedName("requisitosClima") val requisitosClima: String?,
    @SerializedName("requisitosAgua") val requisitosAgua: String?,
    @SerializedName("requisitosLuz") val requisitosLuz: String?,
    @SerializedName("esFavorito") val esFavorito: Boolean,
    @SerializedName("temporadas") val temporadas: List<String>
)

// Modelo para la respuesta de la operación de favoritos
data class ToggleFavoritoResponse(
    @SerializedName("message") val message: String,
    @SerializedName("esFavorito") val esFavorito: Boolean
)

// Modelo para las temporadas de filtro
data class Temporada(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("icono") val icono: String,
    val color: Color
)

val temporadasFiltro = listOf(
    Temporada(id = 1, nombre = "Primavera", icono = "🌸", color = Color(0xFFF9B8C5)), // Rosa
    Temporada(id = 2, nombre = "Verano", icono = "☀️", color = Color(0xFFFFE066)), // Amarillo
    Temporada(id = 3, nombre = "Otoño", icono = "🍂", color = Color(0xFFF08C00)), // Naranja
    Temporada(id = 4, nombre = "Invierno", icono = "❄️", color = Color(0xFF81D4FA))  // Azul
)