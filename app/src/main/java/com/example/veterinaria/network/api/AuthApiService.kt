package com.example.veterinaria.network.api

import com.example.veterinaria.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginDTO): Response<LoginResponse>
    
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterDTO): Response<RegisterResponse>
}