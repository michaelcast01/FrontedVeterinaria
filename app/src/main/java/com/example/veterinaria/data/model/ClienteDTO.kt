package com.example.veterinaria.data.model

import com.google.gson.annotations.SerializedName

data class ClienteDTO(
    @SerializedName("id")
    val id: Long? = null,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("apellido")
    val apellido: String,
    
    @SerializedName("documento")
    val documento: String? = null,
    
    @SerializedName("telefono")
    val telefono: String? = null,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("direccion")
    val direccion: String? = null,
    
    @SerializedName("activo")
    val activo: Boolean = true,
    
    @SerializedName("totalMascotas")
    val totalMascotas: Int = 0
)