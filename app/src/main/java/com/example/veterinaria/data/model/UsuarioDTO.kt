package com.example.veterinaria.data.model

import com.google.gson.annotations.SerializedName

data class UsuarioDTO(
    @SerializedName("id")
    val id: Long? = null,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("apellido")
    val apellido: String,
    
    @SerializedName("telefono")
    val telefono: String? = null,
    
    @SerializedName("tipoUsuario")
    val tipoUsuario: String,
    
    @SerializedName("activo")
    val activo: Boolean = true
)