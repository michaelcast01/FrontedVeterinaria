package com.example.veterinaria.data.repository

import com.example.veterinaria.data.model.CitaDTO
import com.example.veterinaria.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CitaRepository(private val authRepository: AuthRepository) {
    
    private val citaApiService = NetworkModule.provideCitaApiService {
        runCatching { 
            kotlinx.coroutines.runBlocking { authRepository.getToken() }
        }.getOrNull()
    }
    
    suspend fun createCita(cita: CitaDTO): Result<CitaDTO> {
        return try {
            val response = citaApiService.createCita(cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear cita"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getAllCitas(): Flow<Result<List<CitaDTO>>> = flow {
        try {
            val response = citaApiService.getAllCitas()
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener citas")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getCitaById(id: Long): Result<CitaDTO> {
        return try {
            val response = citaApiService.getCitaById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Cita no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateCita(id: Long, cita: CitaDTO): Result<CitaDTO> {
        return try {
            val response = citaApiService.updateCita(id, cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar cita"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteCita(id: Long): Result<Unit> {
        return try {
            val response = citaApiService.deleteCita(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar cita"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getCitasByCliente(clienteId: Long): Flow<Result<List<CitaDTO>>> = flow {
        try {
            val response = citaApiService.getCitasByCliente(clienteId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener citas del cliente")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getCitasByMascota(mascotaId: Long): Flow<Result<List<CitaDTO>>> = flow {
        try {
            val response = citaApiService.getCitasByMascota(mascotaId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener citas de la mascota")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun updateEstadoCita(id: Long, estado: String): Result<CitaDTO> {
        return try {
            val response = citaApiService.updateEstadoCita(id, estado)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar estado de la cita"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}