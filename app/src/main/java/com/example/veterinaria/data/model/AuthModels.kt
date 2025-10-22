package com.example.veterinaria.data.model

import com.google.gson.annotations.SerializedName

data class LoginDTO(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("usuario")
    val usuario: UsuarioDTO?,
    
    @SerializedName("token")
    val token: String?
)

data class RegisterDTO(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("apellido")
    val apellido: String,
    
    @SerializedName("telefono")
    val telefono: String,
    
    @SerializedName("tipoUsuario")
    val tipoUsuario: String
)

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("usuario")
    val usuario: UsuarioDTO?
)