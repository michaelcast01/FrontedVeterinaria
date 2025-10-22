package com.example.veterinaria.util

object Constants {
    
    // URLs base (configurar según entorno)
    object Network {
        const val BASE_URL_DEV = "http://10.0.2.2:8081/api/" // Para emulador Android
        const val BASE_URL_LOCAL = "http://localhost:8081/api/"
        const val BASE_URL_STAGING = "http://staging.veterinaria.com/api/"
        const val BASE_URL_PROD = "http://api.veterinaria.com/api/"
        
        const val TIMEOUT_SECONDS = 30L
    }
    
    // Claves para DataStore
    object DataStore {
        const val AUTH_PREFERENCES = "auth_prefs"
        const val TOKEN_KEY = "auth_token"
        const val USER_ID_KEY = "user_id"
        const val USERNAME_KEY = "username"
        const val USER_TYPE_KEY = "user_type"
    }
    
    // Navegación
    object Navigation {
        const val LOGIN_ROUTE = "login"
        const val REGISTER_ROUTE = "register"
        const val DASHBOARD_ROUTE = "dashboard"
        const val CLIENTES_ROUTE = "clientes"
        const val CLIENTES_DETAIL_ROUTE = "clientes/{clienteId}"
        const val CLIENTES_CREATE_ROUTE = "clientes/create"
        const val CLIENTES_EDIT_ROUTE = "clientes/{clienteId}/edit"
        const val MASCOTAS_ROUTE = "mascotas"
        const val MASCOTAS_DETAIL_ROUTE = "mascotas/{mascotaId}"
        const val MASCOTAS_CREATE_ROUTE = "mascotas/create"
        const val MASCOTAS_EDIT_ROUTE = "mascotas/{mascotaId}/edit"
        const val CITAS_ROUTE = "citas"
        const val CITAS_DETAIL_ROUTE = "citas/{citaId}"
        const val CITAS_CREATE_ROUTE = "citas/create"
        const val CITAS_EDIT_ROUTE = "citas/{citaId}/edit"
    }
    
    // Validaciones
    object Validation {
        const val MIN_PASSWORD_LENGTH = 6
        const val MAX_USERNAME_LENGTH = 50
        const val MAX_NAME_LENGTH = 100
        const val MAX_PHONE_LENGTH = 20
        const val MAX_DOCUMENT_LENGTH = 20
        const val MAX_ADDRESS_LENGTH = 255
        const val MAX_OBSERVATIONS_LENGTH = 500
    }
    
    // Formatos de fecha
    object DateFormats {
        const val ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
        const val ISO_DATE = "yyyy-MM-dd"
        const val DISPLAY_DATE = "dd/MM/yyyy"
        const val DISPLAY_DATE_TIME = "dd/MM/yyyy HH:mm"
        const val DISPLAY_TIME = "HH:mm"
    }
    
    // Estados predefinidos
    object Estados {
        val ESTADOS_CITA = listOf(
            "PENDIENTE" to "Pendiente",
            "CONFIRMADA" to "Confirmada",
            "EN_PROCESO" to "En Proceso",
            "COMPLETADA" to "Completada",
            "CANCELADA" to "Cancelada"
        )
        
        val TIPOS_USUARIO = listOf(
            "CLIENTE" to "Cliente",
            "ADMIN" to "Administrador",
            "VETERINARIO" to "Veterinario",
        )
        
        val SEXOS_MASCOTA = listOf(
            "MACHO" to "Macho",
            "HEMBRA" to "Hembra"
        )
    }
    
    // Especies y razas predefinidas (backup si el backend no responde)
    object EspeciesRazas {
        val ESPECIES_DEFAULT = listOf(
            "Perro", "Gato", "Ave", "Pez", "Hamster", "Conejo", "Otro"
        )
        
        val RAZAS_PERRO = listOf(
            "Labrador", "Golden Retriever", "Pastor Alemán", "Bulldog", 
            "Beagle", "Poodle", "Rottweiler", "Yorkshire Terrier", "Mestizo", "Otro"
        )
        
        val RAZAS_GATO = listOf(
            "Persa", "Siamés", "Maine Coon", "Ragdoll", "Bengalí", 
            "Esfinge", "Angora", "Común Europeo", "Mestizo", "Otro"
        )
    }
    
    // Mensajes de error comunes
    object ErrorMessages {
        const val NETWORK_ERROR = "Error de conexión. Verifica tu conexión a internet."
        const val SERVER_ERROR = "Error del servidor. Intenta más tarde."
        const val UNAUTHORIZED = "Sesión expirada. Vuelve a iniciar sesión."
        const val NOT_FOUND = "Recurso no encontrado."
        const val VALIDATION_ERROR = "Por favor, verifica los datos ingresados."
        const val UNKNOWN_ERROR = "Ha ocurrido un error inesperado."
    }
}