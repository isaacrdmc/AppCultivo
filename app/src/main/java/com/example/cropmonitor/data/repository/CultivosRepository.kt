package com.example.cropmonitor.data.repository

import com.example.cropmonitor.data.api.CultivosApiService
import com.example.cropmonitor.data.models.CultivoDetailDto
import com.example.cropmonitor.data.models.CultivoListDto
import com.example.cropmonitor.data.models.ToggleFavoritoResponse

class CultivosRepository(private val apiService: CultivosApiService) {

    suspend fun getCultivos(temporadaId: Int? = null, searchText: String? = null): Result<List<CultivoListDto>> {
        return try {
            val response = apiService.getCultivos(temporadaId, searchText)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener cultivos: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteCultivos(): Result<List<CultivoListDto>> {
        return try {
            val response = apiService.getCultivosFavoritos()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía de favoritos."))
            } else {
                Result.failure(Exception("Error al cargar favoritos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCultivoById(id: Int): Result<CultivoDetailDto> {
        return try {
            val response = apiService.getCultivoById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener detalle del cultivo: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorito(cultivoId: Int): Result<ToggleFavoritoResponse> {
        return try {
            val response = apiService.toggleFavorito(cultivoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar favorito: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}