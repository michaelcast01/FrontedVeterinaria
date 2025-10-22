package com.example.veterinaria.data.repository

import com.example.veterinaria.data.model.MascotaDTO
import com.example.veterinaria.data.model.EstadisticasMascotasResponse
import com.example.veterinaria.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MascotaRepository(private val authRepository: AuthRepository) {
    
    private val mascotaApiService = NetworkModule.provideMascotaApiService {
        runCatching { 
            kotlinx.coroutines.runBlocking { authRepository.getToken() }
        }.getOrNull()
    }
    
    fun getAllMascotas(): Flow<Result<List<MascotaDTO>>> = flow {
        try {
            val response = mascotaApiService.getAllMascotas()
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener mascotas")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getMascotasActivas(): Flow<Result<List<MascotaDTO>>> = flow {
        try {
            val response = mascotaApiService.getMascotasActivas()
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener mascotas activas")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getMascotaById(id: Long): Result<MascotaDTO> {
        return try {
            val response = mascotaApiService.getMascotaById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Mascota no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getMascotasByCliente(clienteId: Long): Flow<Result<List<MascotaDTO>>> = flow {
        try {
            val response = mascotaApiService.getMascotasByCliente(clienteId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener mascotas del cliente")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun buscarMascotas(query: String): Flow<Result<List<MascotaDTO>>> = flow {
        try {
            val response = mascotaApiService.buscarMascotas(query)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error en la búsqueda")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getEspecies(): Result<List<String>> {
        return try {
            val response = mascotaApiService.getEspecies()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener especies"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRazasByEspecie(especie: String): Result<List<String>> {
        return try {
            val response = mascotaApiService.getRazasByEspecie(especie)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener razas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createMascota(mascota: MascotaDTO): Result<MascotaDTO> {
        return try {
            val response = mascotaApiService.createMascota(mascota)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear mascota"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateMascota(id: Long, mascota: MascotaDTO): Result<MascotaDTO> {
        return try {
            val response = mascotaApiService.updateMascota(id, mascota)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar mascota"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteMascota(id: Long): Result<Unit> {
        return try {
            val response = mascotaApiService.deleteMascota(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar mascota"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun activarMascota(id: Long): Result<Unit> {
        return try {
            val response = mascotaApiService.activarMascota(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al activar mascota"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getEstadisticasMascotas(): Result<EstadisticasMascotasResponse> {
        return try {
            val response = mascotaApiService.getEstadisticasMascotas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener estadísticas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}