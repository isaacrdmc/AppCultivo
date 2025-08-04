package com.example.cropmonitor.data.api

import com.example.cropmonitor.data.models.MedidorSlotDto
import com.example.cropmonitor.data.models.ModuloCreateDto
import com.example.cropmonitor.data.models.ModuloListDto
import com.example.cropmonitor.data.models.SensorDetailDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ModulosApiService {
    // Endpoint para obtener la lista de módulos del usuario autenticado
    @GET("Modulos")
    suspend fun getModulosUsuario(): Response<List<ModuloListDto>>

    // Endpoint para crear un nuevo módulo
    @POST("Modulos")
    suspend fun createModulo(@Body modulo: ModuloCreateDto): Response<ModuloListDto>

    // Endpoint para obtener los sensores de un módulo específico
    @GET("Modulos/{moduloId}/sensores")
    suspend fun getSensoresByModulo(@Path("moduloId") moduloId: Int): Response<List<MedidorSlotDto>>

    // Endpoint para obtener el detalle de un sensor específico
    @GET("Modulos/sensores/{sensorId}")
    suspend fun getSensorDetail(@Path("sensorId") sensorId: Int): Response<SensorDetailDto>

    // Código NUEVO y CORREGIDO para el endpoint existente del backend
    @PUT("Modulos/{moduloId}/medidores/{medidorSlotIndex}/asignar-cultivo/{cultivoId}")
    suspend fun asignarCultivoAMedidorSlot(
        @Path("moduloId") moduloId: Int,
        @Path("medidorSlotIndex") medidorSlotIndex: Int,
        @Path("cultivoId") cultivoId: Int
    ): Response<Unit>

    // Método para desasignar un cultivo, si lo necesitas
    @PUT("Modulos/{moduloId}/medidores/{medidorSlotIndex}/desasignar-cultivo")
    suspend fun desasignarCultivoDeMedidorSlot(
        @Path("moduloId") moduloId: Int,
        @Path("medidorSlotIndex") medidorSlotIndex: Int
    ): Response<Unit>
}