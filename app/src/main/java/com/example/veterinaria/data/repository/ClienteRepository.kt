package com.example.veterinaria.data.repository

import com.example.veterinaria.data.model.ClienteDTO
import com.example.veterinaria.data.model.EstadisticasClientesResponse
import com.example.veterinaria.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClienteRepository(private val authRepository: AuthRepository) {
    
    private val clienteApiService = NetworkModule.provideClienteApiService {
        // Esto será resuelto cuando se llame a la función
        runCatching { 
            kotlinx.coroutines.runBlocking { authRepository.getToken() }
        }.getOrNull()
    }
    
    fun getAllClientes(): Flow<Result<List<ClienteDTO>>> = flow {
        try {
            val response = clienteApiService.getAllClientes()
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener clientes")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getClientesActivos(): Flow<Result<List<ClienteDTO>>> = flow {
        try {
            val response = clienteApiService.getClientesActivos()
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error al obtener clientes activos")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getClienteById(id: Long): Result<ClienteDTO> {
        return try {
            val response = clienteApiService.getClienteById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Cliente no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getClienteByDocumento(documento: String): Result<ClienteDTO> {
        return try {
            val response = clienteApiService.getClienteByDocumento(documento)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Cliente no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun buscarClientes(query: String): Flow<Result<List<ClienteDTO>>> = flow {
        try {
            val response = clienteApiService.buscarClientes(query)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error en la búsqueda")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun createCliente(cliente: ClienteDTO): Result<ClienteDTO> {
        return try {
            val response = clienteApiService.createCliente(cliente)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear cliente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateCliente(id: Long, cliente: ClienteDTO): Result<ClienteDTO> {
        return try {
            val response = clienteApiService.updateCliente(id, cliente)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar cliente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteCliente(id: Long): Result<Unit> {
        return try {
            val response = clienteApiService.deleteCliente(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar cliente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun activarCliente(id: Long): Result<Unit> {
        return try {
            val response = clienteApiService.activarCliente(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al activar cliente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getEstadisticasClientes(): Result<EstadisticasClientesResponse> {
        return try {
            val response = clienteApiService.getEstadisticasClientes()
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