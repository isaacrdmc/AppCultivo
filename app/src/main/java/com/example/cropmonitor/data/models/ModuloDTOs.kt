package com.example.cropmonitor.data.models

import com.google.gson.annotations.SerializedName

// Pantalla 1: Lista de Módulos
data class ModuloListDto(
    @SerializedName("ModuloID") val moduloID: Int,
    @SerializedName("NombreModulo") val nombreModulo: String,
    @SerializedName("Estado") val estado: String,
    @SerializedName("DiasEnFuncionamiento") val diasEnFuncionamiento: Int,
    @SerializedName("CantidadCultivosActual") val cantidadCultivosActual: Int,
    @SerializedName("CantidadCultivosMax") val cantidadCultivosMax: Int
)

// DTO para crear un nuevo módulo
data class ModuloCreateDto(
    @SerializedName("NombreModulo") val nombreModulo: String,
    @SerializedName("Estado") val estado: String,
    @SerializedName("DiasEnFuncionamiento") val diasEnFuncionamiento: Int
)

// DTO para representar el slot de un medidor (coincide con el back-end)
data class MedidorSlotDto(
    @SerializedName("MedidorSlotIndex") val medidorSlotIndex: Int,
    @SerializedName("EnUso") val enUso: Boolean,
    @SerializedName("CultivoID") val cultivoID: Int?,
    @SerializedName("CultivoNombre") val cultivoNombre: String?,
    @SerializedName("CultivoImagenURL") val cultivoImagenURL: String?,
    @SerializedName("Sensores") val sensores: List<SensorLecturaDto>
)

// DTO para la lectura de un sensor dentro de un slot (coincide con el back-end)
data class SensorLecturaDto(
    @SerializedName("SensorID") val sensorID: Int,
    @SerializedName("TipoSensor") val tipoSensor: String,
    @SerializedName("UnidadMedida") val unidadMedida: String,
    @SerializedName("ValorLectura") val valorLectura: Double?,
    @SerializedName("EstadoRiego") val estadoRiego: String?
)

// Pantalla 3: Detalle de un medidor/sensor
data class SensorDetailDto(
    @SerializedName("SensorID") val sensorID: Int,
    @SerializedName("ModuloNombre") val moduloNombre: String,
    @SerializedName("TipoSensor") val tipoSensor: String,
    @SerializedName("UnidadMedida") val unidadMedida: String,
    @SerializedName("ValorLectura") val valorLectura: Double?,
    @SerializedName("UltimaLectura") val ultimaLectura: String?,
    @SerializedName("EstadoRiego") val estadoRiego: String?,
    @SerializedName("EsAcuaHidroponico") val esAcuaHidroponico: Boolean,

    // Detalles del cultivo
    @SerializedName("CultivoID") val cultivoID: Int?,
    @SerializedName("CultivoNombre") val cultivoNombre: String?,
    @SerializedName("CultivoImagenURL") val cultivoImagenURL: String?,
    @SerializedName("CultivoRequisitosClima") val cultivoRequisitosClima: String?,
    @SerializedName("CultivoRequisitosAgua") val cultivoRequisitosAgua: String?,
    @SerializedName("CultivoRequisitosLuz") val cultivoRequisitosLuz: String?,

    // Tips
    @SerializedName("TipsParaEstaPlanta") val tipsParaEstaPlanta: List<String>?
)