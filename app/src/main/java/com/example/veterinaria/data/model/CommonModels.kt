package com.example.veterinaria.data.model

import com.google.gson.annotations.SerializedName

data class EstadisticasClientesResponse(
    @SerializedName("totalClientesActivos")
    val totalClientesActivos: Int
)

data class EstadisticasMascotasResponse(
    @SerializedName("totalMascotasActivas")
    val totalMascotasActivas: Int
)

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: T? = null
)

// Estados para las citas
enum class EstadoCita(val valor: String, val descripcion: String) {
    PENDIENTE("PENDIENTE", "Pendiente"),
    CONFIRMADA("CONFIRMADA", "Confirmada"),
    EN_PROCESO("EN_PROCESO", "En Proceso"),
    COMPLETADA("COMPLETADA", "Completada"),
    CANCELADA("CANCELADA", "Cancelada")
}

// Tipos de usuario
enum class TipoUsuario(val valor: String, val descripcion: String) {
    ADMIN("ADMIN", "Administrador"),
    VETERINARIO("VETERINARIO", "Veterinario"),
    RECEPCIONISTA("RECEPCIONISTA", "Recepcionista")
}

// Sexos de mascotas
enum class SexoMascota(val valor: String, val descripcion: String) {
    MACHO("MACHO", "Macho"),
    HEMBRA("HEMBRA", "Hembra")
}