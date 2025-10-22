package com.example.veterinaria.data.model

import com.google.gson.annotations.SerializedName

data class CitaDTO(
    @SerializedName("id")
    val id: Long? = null,
    
    @SerializedName("fechaHora")
    val fechaHora: String, // ISO-8601 datetime format
    
    @SerializedName("estado")
    val estado: String,
    
    @SerializedName("motivo")
    val motivo: String? = null,
    
    @SerializedName("observaciones")
    val observaciones: String? = null,
    
    @SerializedName("costo")
    val costo: Double? = null,
    
    @SerializedName("clienteId")
    val clienteId: Long,
    
    @SerializedName("mascotaId")
    val mascotaId: Long,
    
    @SerializedName("veterinarioId")
    val veterinarioId: Long? = null,
    
    @SerializedName("clienteNombre")
    val clienteNombre: String? = null,
    
    @SerializedName("mascotaNombre")
    val mascotaNombre: String? = null,
    
    @SerializedName("veterinarioNombre")
    val veterinarioNombre: String? = null
)