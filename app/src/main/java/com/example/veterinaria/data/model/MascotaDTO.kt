package com.example.veterinaria.data.model

import com.google.gson.annotations.SerializedName

data class MascotaDTO(
    @SerializedName("id")
    val id: Long? = null,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("especie")
    val especie: String,
    
    @SerializedName("raza")
    val raza: String? = null,
    
    @SerializedName("sexo")
    val sexo: String? = null,
    
    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String? = null, // ISO-8601 date format
    
    @SerializedName("peso")
    val peso: Double? = null,
    
    @SerializedName("color")
    val color: String? = null,
    
    @SerializedName("observaciones")
    val observaciones: String? = null,
    
    @SerializedName("activo")
    val activo: Boolean = true,
    
    @SerializedName("clienteId")
    val clienteId: Long,
    
    @SerializedName("clienteNombre")
    val clienteNombre: String? = null,
    
    @SerializedName("edad")
    val edad: Int? = null
)