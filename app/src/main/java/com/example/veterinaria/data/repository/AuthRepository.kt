package com.example.veterinaria.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.veterinaria.data.model.*
import com.example.veterinaria.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthRepository(private val context: Context) {
    
    private val authApiService = NetworkModule.provideAuthApiService()
    
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USER_TYPE_KEY = stringPreferencesKey("user_type")
    }
    
    // Flows para observar el estado de autenticación
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        !preferences[TOKEN_KEY].isNullOrEmpty()
    }
    
    val currentUser: Flow<UsuarioDTO?> = context.dataStore.data.map { preferences ->
        val userId = preferences[USER_ID_KEY]
        val username = preferences[USERNAME_KEY]
        val userType = preferences[USER_TYPE_KEY]
        
        if (!userId.isNullOrEmpty() && !username.isNullOrEmpty() && !userType.isNullOrEmpty()) {
            UsuarioDTO(
                id = userId.toLongOrNull(),
                username = username,
                email = "", // Se llenará con datos completos del backend
                nombre = "",
                apellido = "",
                tipoUsuario = userType
            )
        } else {
            null
        }
    }
    
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = authApiService.login(LoginDTO(username, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                if (loginResponse.success && loginResponse.token != null) {
                    // Guardar datos de sesión
                    saveUserSession(loginResponse)
                    Result.success(loginResponse)
                } else {
                    Result.failure(Exception(loginResponse.message))
                }
            } else {
                Result.failure(Exception("Error en la comunicación con el servidor"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(registerData: RegisterDTO): Result<RegisterResponse> {
        return try {
            val response = authApiService.register(registerData)
            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body()!!
                if (registerResponse.success) {
                    Result.success(registerResponse)
                } else {
                    Result.failure(Exception(registerResponse.message))
                }
            } else {
                Result.failure(Exception("Error en el registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    suspend fun getToken(): String? {
        return context.dataStore.data.first()[TOKEN_KEY]
    }
    
    private suspend fun saveUserSession(loginResponse: LoginResponse) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = loginResponse.token ?: ""
            loginResponse.usuario?.let { user ->
                preferences[USER_ID_KEY] = user.id?.toString() ?: ""
                preferences[USERNAME_KEY] = user.username
                preferences[USER_TYPE_KEY] = user.tipoUsuario
            }
        }
    }
}