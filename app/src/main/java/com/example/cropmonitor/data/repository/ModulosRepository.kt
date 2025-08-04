package com.example.cropmonitor.data.repository

import com.example.cropmonitor.data.api.ModulosApiService
import com.example.cropmonitor.data.models.ModuloCreateDto
import com.example.cropmonitor.data.models.ModuloListDto
import com.example.cropmonitor.data.models.MedidorSlotDto
import com.example.cropmonitor.data.models.SensorDetailDto
import retrofit2.Response
import java.io.IOException

class ModulosRepository(
    private val modulosApiService: ModulosApiService
) {
    suspend fun getModulosUsuario(): Response<List<ModuloListDto>> {
        return modulosApiService.getModulosUsuario()
    }

    suspend fun createModulo(modulo: ModuloCreateDto): Response<ModuloListDto> {
        return modulosApiService.createModulo(modulo)
    }


    suspend fun asignarCultivoAMedidorSlot(moduloId: Int, medidorSlotIndex: Int, cultivoId: Int) {
        val response = modulosApiService.asignarCultivoAMedidorSlot(moduloId, medidorSlotIndex, cultivoId)
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string() ?: "Error desconocido"
            throw IOException("Error al asignar cultivo: ${response.code()} - $errorBody")
        }
    }


    suspend fun getSensoresByModulo(moduloId: Int): List<MedidorSlotDto> {
        val response = modulosApiService.getSensoresByModulo(moduloId)

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw IOException("Error al obtener los slots del módulo: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getSensorDetail(sensorId: Int): SensorDetailDto {
        val response = modulosApiService.getSensorDetail(sensorId)

        if (response.isSuccessful) {
            return response.body() ?: throw IOException("Cuerpo de la respuesta nulo para el sensor con ID: $sensorId")
        } else {
            throw IOException("Error al obtener el detalle del sensor: ${response.code()} - ${response.message()}")
        }
    }
}