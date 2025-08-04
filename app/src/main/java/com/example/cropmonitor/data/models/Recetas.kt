package com.example.cropmonitor.data.models

import kotlinx.serialization.Serializable

// Modelos de datos para las recetas
@Serializable
data class RecetaListDto(
    val RecetaID: Int,
    val NombreReceta: String,
    val DescripcionCorta: String?
)
// Modelo para los detalles de una receta
@Serializable
data class RecetaDetailDto(
    val RecetaID: Int,
    val NombreReceta: String,
    val Descripcion: String?,
    val Instrucciones: String?,
    val CultivosNecesarios: List<CultivoEnRecetaDto>
)
// Modelo para los cultivos necesarios en una receta
@Serializable
data class CultivoEnRecetaDto(
    val CultivoID: Int,
    val NombreCultivo: String
)