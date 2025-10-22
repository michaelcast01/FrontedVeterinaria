package com.example.veterinaria.network.api

import com.example.veterinaria.data.model.ClienteDTO
import com.example.veterinaria.data.model.EstadisticasClientesResponse
import retrofit2.Response
import retrofit2.http.*

interface ClienteApiService {
    
    @GET("clientes")
    suspend fun getAllClientes(): Response<List<ClienteDTO>>
    
    @GET("clientes/activos")
    suspend fun getClientesActivos(): Response<List<ClienteDTO>>
    
    @GET("clientes/{id}")
    suspend fun getClienteById(@Path("id") id: Long): Response<ClienteDTO>
    
    @GET("clientes/documento/{documento}")
    suspend fun getClienteByDocumento(@Path("documento") documento: String): Response<ClienteDTO>
    
    @GET("clientes/buscar")
    suspend fun buscarClientes(@Query("q") query: String): Response<List<ClienteDTO>>
    
    @POST("clientes")
    suspend fun createCliente(@Body cliente: ClienteDTO): Response<ClienteDTO>
    
    @PUT("clientes/{id}")
    suspend fun updateCliente(@Path("id") id: Long, @Body cliente: ClienteDTO): Response<ClienteDTO>
    
    @DELETE("clientes/{id}")
    suspend fun deleteCliente(@Path("id") id: Long): Response<Void>
    
    @PATCH("clientes/{id}/activar")
    suspend fun activarCliente(@Path("id") id: Long): Response<Void>
    
    @GET("clientes/estadisticas")
    suspend fun getEstadisticasClientes(): Response<EstadisticasClientesResponse>
}