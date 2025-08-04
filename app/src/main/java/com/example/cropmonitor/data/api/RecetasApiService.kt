package com.example.cropmonitor.data.api

import com.example.cropmonitor.data.models.RecetaDetailDto
import com.example.cropmonitor.data.models.RecetaListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecetasApiService {
    //endpoint para obtener todas las recetas
    @GET("Recetas")
    suspend fun getRecetas(): List<RecetaListDto>
    // endpoint para obtener una receta específica por ID
    @GET("Recetas/{id}")
    suspend fun getReceta(@Path("id") recetaId: Int): RecetaDetailDto
    // endpoint para buscar recetas por nombre o descripción
    @GET("Recetas/search")
    suspend fun searchRecetas(@Query("query") query: String): List<RecetaListDto>
}