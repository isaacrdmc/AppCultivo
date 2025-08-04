package com.example.cropmonitor.data.repository

import com.example.cropmonitor.data.models.RecetaDetailDto
import com.example.cropmonitor.data.models.RecetaListDto
import com.example.cropmonitor.data.api.RecetasApiService

interface RecetasRepository {
    suspend fun getRecetas(): List<RecetaListDto>
    suspend fun getReceta(recetaId: Int): RecetaDetailDto
    suspend fun searchRecetas(query: String): List<RecetaListDto>
}

class NetworkRecetasRepository(
    private val recetasApiService: RecetasApiService
) : RecetasRepository {
    override suspend fun getRecetas(): List<RecetaListDto> = recetasApiService.getRecetas()
    override suspend fun getReceta(recetaId: Int): RecetaDetailDto = recetasApiService.getReceta(recetaId)
    override suspend fun searchRecetas(query: String): List<RecetaListDto> = recetasApiService.searchRecetas(query)
}