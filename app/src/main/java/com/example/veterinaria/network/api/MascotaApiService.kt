package com.example.veterinaria.network.api

import com.example.veterinaria.data.model.MascotaDTO
import com.example.veterinaria.data.model.EstadisticasMascotasResponse
import retrofit2.Response
import retrofit2.http.*

interface MascotaApiService {
    
    @GET("mascotas")
    suspend fun getAllMascotas(): Response<List<MascotaDTO>>
    
    @GET("mascotas/activas")
    suspend fun getMascotasActivas(): Response<List<MascotaDTO>>
    
    @GET("mascotas/{id}")
    suspend fun getMascotaById(@Path("id") id: Long): Response<MascotaDTO>
    
    @GET("mascotas/cliente/{clienteId}")
    suspend fun getMascotasByCliente(@Path("clienteId") clienteId: Long): Response<List<MascotaDTO>>
    
    @GET("mascotas/buscar")
    suspend fun buscarMascotas(@Query("q") query: String): Response<List<MascotaDTO>>
    
    @GET("mascotas/especies")
    suspend fun getEspecies(): Response<List<String>>
    
    @GET("mascotas/razas/{especie}")
    suspend fun getRazasByEspecie(@Path("especie") especie: String): Response<List<String>>
    
    @POST("mascotas")
    suspend fun createMascota(@Body mascota: MascotaDTO): Response<MascotaDTO>
    
    @PUT("mascotas/{id}")
    suspend fun updateMascota(@Path("id") id: Long, @Body mascota: MascotaDTO): Response<MascotaDTO>
    
    @DELETE("mascotas/{id}")
    suspend fun deleteMascota(@Path("id") id: Long): Response<Void>
    
    @PATCH("mascotas/{id}/activar")
    suspend fun activarMascota(@Path("id") id: Long): Response<Void>
    
    @GET("mascotas/estadisticas")
    suspend fun getEstadisticasMascotas(): Response<EstadisticasMascotasResponse>
}