package com.example.veterinaria.network.api

import com.example.veterinaria.data.model.CitaDTO
import retrofit2.Response
import retrofit2.http.*

interface CitaApiService {
    
    @POST("citas")
    suspend fun createCita(@Body cita: CitaDTO): Response<CitaDTO>
    
    @GET("citas")
    suspend fun getAllCitas(): Response<List<CitaDTO>>
    
    @GET("citas/{id}")
    suspend fun getCitaById(@Path("id") id: Long): Response<CitaDTO>
    
    @PUT("citas/{id}")
    suspend fun updateCita(@Path("id") id: Long, @Body cita: CitaDTO): Response<CitaDTO>
    
    @DELETE("citas/{id}")
    suspend fun deleteCita(@Path("id") id: Long): Response<Void>
    
    @GET("citas/cliente/{clienteId}")
    suspend fun getCitasByCliente(@Path("clienteId") clienteId: Long): Response<List<CitaDTO>>
    
    @GET("citas/mascota/{mascotaId}")
    suspend fun getCitasByMascota(@Path("mascotaId") mascotaId: Long): Response<List<CitaDTO>>
    
    @PATCH("citas/{id}/estado")
    suspend fun updateEstadoCita(@Path("id") id: Long, @Query("estado") estado: String): Response<CitaDTO>
}