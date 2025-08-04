package com.example.cropmonitor.data.api

import com.example.cropmonitor.data.models.CultivoDetailDto
import com.example.cropmonitor.data.models.CultivoListDto
import com.example.cropmonitor.data.models.ToggleFavoritoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CultivosApiService {
    // Endpoint para obtener la lista de cultivos
    // Se puede filtrar por temporadaId y/o searchText
    @GET("Cultivos")
    suspend fun getCultivos(
        @Query("temporadaId") temporadaId: Int?,
        @Query("searchText") searchText: String?
    ): Response<List<CultivoListDto>>
    // Endpoint para obtener los cultivos favoritos del usuario
    @GET("Cultivos/Favoritos")
    suspend fun getCultivosFavoritos(): Response<List<CultivoListDto>>
    // Endpoint para obtener los detalles de un cultivo específico por ID
    @GET("Cultivos/{id}")
    suspend fun getCultivoById(@Path("id") id: Int): Response<CultivoDetailDto>
    // Endpoint para obtener los cultivos de un usuario autenticado
    @POST("Cultivos/{cultivoId}/toggle-favorito")
    suspend fun toggleFavorito(@Path("cultivoId") cultivoId: Int): Response<ToggleFavoritoResponse>
}