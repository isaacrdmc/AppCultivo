package com.example.cropmonitor.network

import LoginRequestDto
import LoginResponseDto
import RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("Auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @POST("Auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<Unit>
}