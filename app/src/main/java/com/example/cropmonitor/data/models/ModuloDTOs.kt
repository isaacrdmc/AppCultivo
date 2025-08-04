package com.example.cropmonitor.data.models

import com.google.gson.annotations.SerializedName

// Pantalla 1: Lista de Módulos
data class ModuloListDto(
    @SerializedName("moduloID") val moduloID: Int,
    @SerializedName("nombreModulo") val nombreModulo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("diasEnFuncionamiento") val diasEnFuncionamiento: Int,
    @SerializedName("cantidadCultivosActual") val cantidadCultivosActual: Int,
    @SerializedName("cantidadCultivosMax") val cantidadCultivosMax: Int
)

// DTO para crear un nuevo módulo
data class ModuloCreateDto(
    @SerializedName("nombreModulo") val nombreModulo: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("diasEnFuncionamiento") val diasEnFuncionamiento: Int
)

// DTO para representar el slot de un medidor (coincide con el back-end)
data class MedidorSlotDto(
    @SerializedName("medidorSlotIndex") val medidorSlotIndex: Int,
    @SerializedName("enUso") val enUso: Boolean,
    @SerializedName("cultivoID") val cultivoID: Int?,
    @SerializedName("cultivoNombre") val cultivoNombre: String?,
    @SerializedName("cultivoImagenURL") val cultivoImagenURL: String?,
    @SerializedName("sensores") val sensores: List<SensorLecturaDto>
)

// DTO para la lectura de un sensor dentro de un slot (coincide con el back-end)
data class SensorLecturaDto(
    @SerializedName("sensorID") val sensorID: Int,
    @SerializedName("tipoSensor") val tipoSensor: String,
    @SerializedName("unidadMedida") val unidadMedida: String,
    @SerializedName("valorLectura") val valorLectura: Double?,
    @SerializedName("estadoRiego") val estadoRiego: String?
)

// Pantalla 3: Detalle de un medidor/sensor
data class SensorDetailDto(
    @SerializedName("sensorID") val sensorID: Int,
    @SerializedName("moduloNombre") val moduloNombre: String,
    @SerializedName("tipoSensor") val tipoSensor: String,
    @SerializedName("unidadMedida") val unidadMedida: String,
    @SerializedName("valorLectura") val valorLectura: Double?,
    @SerializedName("ultimaLectura") val ultimaLectura: String?,
    @SerializedName("estadoRiego") val estadoRiego: String?,
    @SerializedName("esAcuaHidroponico") val esAcuaHidroponico: Boolean,

    // Detalles del cultivo
    @SerializedName("cultivoID") val cultivoID: Int?,
    @SerializedName("cultivoNombre") val cultivoNombre: String?,
    @SerializedName("cultivoImagenURL") val cultivoImagenURL: String?,
    @SerializedName("cultivoRequisitosClima") val cultivoRequisitosClima: String?,
    @SerializedName("cultivoRequisitosAgua") val cultivoRequisitosAgua: String?,
    @SerializedName("cultivoRequisitosLuz") val cultivoRequisitosLuz: String?,

    // Tips
    @SerializedName("tipsParaEstaPlanta") val tipsParaEstaPlanta: List<String>?
)