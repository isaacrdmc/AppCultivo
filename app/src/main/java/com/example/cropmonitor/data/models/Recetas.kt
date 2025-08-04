package com.example.cropmonitor.data.models

import kotlinx.serialization.Serializable

// Modelos de datos para las recetas
@Serializable
data class RecetaListDto(
    val recetaID: Int,
    val nombreReceta: String,
    val descripcionCorta: String?
)
// Modelo para los detalles de una receta
@Serializable
data class RecetaDetailDto(
    val recetaID: Int,
    val nombreReceta: String,
    val descripcion: String?,
    val instrucciones: String?,
    val cultivosNecesarios: List<CultivoEnRecetaDto>
)
// Modelo para los cultivos necesarios en una receta
@Serializable
data class CultivoEnRecetaDto(
    val cultivoID: Int,
    val nombreCultivo: String
)