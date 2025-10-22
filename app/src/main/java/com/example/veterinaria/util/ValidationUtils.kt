package com.example.veterinaria.util

import android.util.Patterns
import com.example.veterinaria.util.Constants.Validation
import java.util.regex.Pattern

object ValidationUtils {
    
    // Patrones de validación
    private val phonePattern = Pattern.compile("^[+]?[0-9]{7,15}$")
    private val documentPattern = Pattern.compile("^[0-9A-Za-z-]{5,20}$")
    private val namePattern = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,}$")
    
    /**
     * Valida un email
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Valida una contraseña
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= Validation.MIN_PASSWORD_LENGTH
    }
    
    /**
     * Valida un nombre de usuario
     */
    fun isValidUsername(username: String): Boolean {
        return username.isNotEmpty() && 
               username.length <= Validation.MAX_USERNAME_LENGTH &&
               username.matches(Regex("^[a-zA-Z0-9._-]+$"))
    }
    
    /**
     * Valida un nombre o apellido
     */
    fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && 
               name.length <= Validation.MAX_NAME_LENGTH &&
               namePattern.matcher(name).matches()
    }
    
    /**
     * Valida un número de teléfono
     */
    fun isValidPhone(phone: String): Boolean {
        return phone.isEmpty() || (phone.length <= Validation.MAX_PHONE_LENGTH && 
               phonePattern.matcher(phone).matches())
    }
    
    /**
     * Valida un documento de identidad
     */
    fun isValidDocument(document: String): Boolean {
        return document.isEmpty() || (document.length <= Validation.MAX_DOCUMENT_LENGTH && 
               documentPattern.matcher(document).matches())
    }
    
    /**
     * Valida una dirección
     */
    fun isValidAddress(address: String): Boolean {
        return address.isEmpty() || address.length <= Validation.MAX_ADDRESS_LENGTH
    }
    
    /**
     * Valida observaciones
     */
    fun isValidObservations(observations: String): Boolean {
        return observations.isEmpty() || observations.length <= Validation.MAX_OBSERVATIONS_LENGTH
    }
    
    /**
     * Valida un peso (debe ser positivo)
     */
    fun isValidWeight(weight: Double?): Boolean {
        return weight == null || weight > 0.0
    }
    
    /**
     * Valida un costo (debe ser positivo)
     */
    fun isValidCost(cost: Double?): Boolean {
        return cost == null || cost >= 0.0
    }
    
    /**
     * Validación de datos de cliente
     */
    data class ClienteValidation(
        val isValid: Boolean,
        val nombreError: String? = null,
        val apellidoError: String? = null,
        val documentoError: String? = null,
        val telefonoError: String? = null,
        val emailError: String? = null,
        val direccionError: String? = null
    )
    
    fun validateCliente(
        nombre: String,
        apellido: String,
        documento: String? = null,
        telefono: String? = null,
        email: String? = null,
        direccion: String? = null
    ): ClienteValidation {
        val nombreError = if (!isValidName(nombre)) "Nombre inválido" else null
        val apellidoError = if (!isValidName(apellido)) "Apellido inválido" else null
        val documentoError = if (!isValidDocument(documento ?: "")) "Documento inválido" else null
        val telefonoError = if (!isValidPhone(telefono ?: "")) "Teléfono inválido" else null
        val emailError = if (!email.isNullOrEmpty() && !isValidEmail(email)) "Email inválido" else null
        val direccionError = if (!isValidAddress(direccion ?: "")) "Dirección muy larga" else null
        
        val isValid = nombreError == null && apellidoError == null && documentoError == null &&
                     telefonoError == null && emailError == null && direccionError == null
        
        return ClienteValidation(
            isValid = isValid,
            nombreError = nombreError,
            apellidoError = apellidoError,
            documentoError = documentoError,
            telefonoError = telefonoError,
            emailError = emailError,
            direccionError = direccionError
        )
    }
    
    /**
     * Validación de datos de mascota
     */
    data class MascotaValidation(
        val isValid: Boolean,
        val nombreError: String? = null,
        val especieError: String? = null,
        val pesoError: String? = null,
        val fechaNacimientoError: String? = null,
        val observacionesError: String? = null
    )
    
    fun validateMascota(
        nombre: String,
        especie: String,
        peso: Double? = null,
        fechaNacimiento: String? = null,
        observaciones: String? = null
    ): MascotaValidation {
        val nombreError = if (!isValidName(nombre)) "Nombre inválido" else null
        val especieError = if (especie.isEmpty()) "Especie requerida" else null
        val pesoError = if (!isValidWeight(peso)) "Peso debe ser positivo" else null
        val fechaNacimientoError = if (!fechaNacimiento.isNullOrEmpty() && 
                                      !DateUtils.isValidDate(fechaNacimiento)) "Fecha inválida" else null
        val observacionesError = if (!isValidObservations(observaciones ?: "")) "Observaciones muy largas" else null
        
        val isValid = nombreError == null && especieError == null && pesoError == null &&
                     fechaNacimientoError == null && observacionesError == null
        
        return MascotaValidation(
            isValid = isValid,
            nombreError = nombreError,
            especieError = especieError,
            pesoError = pesoError,
            fechaNacimientoError = fechaNacimientoError,
            observacionesError = observacionesError
        )
    }
    
    /**
     * Validación de login
     */
    data class LoginValidation(
        val isValid: Boolean,
        val usernameError: String? = null,
        val passwordError: String? = null
    )
    
    fun validateLogin(username: String, password: String): LoginValidation {
        val usernameError = if (!isValidUsername(username)) "Usuario inválido" else null
        val passwordError = if (!isValidPassword(password)) "Contraseña muy corta" else null
        
        return LoginValidation(
            isValid = usernameError == null && passwordError == null,
            usernameError = usernameError,
            passwordError = passwordError
        )
    }
    
    /**
     * Validación de registro
     */
    data class RegisterValidation(
        val isValid: Boolean,
        val usernameError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val nombreError: String? = null,
        val apellidoError: String? = null,
        val telefonoError: String? = null
    )
    
    fun validateRegister(
        username: String,
        email: String,
        password: String,
        nombre: String,
        apellido: String,
        telefono: String
    ): RegisterValidation {
        val usernameError = if (!isValidUsername(username)) "Usuario inválido" else null
        val emailError = if (!isValidEmail(email)) "Email inválido" else null
        val passwordError = if (!isValidPassword(password)) "Contraseña muy corta" else null
        val nombreError = if (!isValidName(nombre)) "Nombre inválido" else null
        val apellidoError = if (!isValidName(apellido)) "Apellido inválido" else null
        val telefonoError = if (!isValidPhone(telefono)) "Teléfono inválido" else null
        
        val isValid = usernameError == null && emailError == null && passwordError == null &&
                     nombreError == null && apellidoError == null && telefonoError == null
        
        return RegisterValidation(
            isValid = isValid,
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError,
            nombreError = nombreError,
            apellidoError = apellidoError,
            telefonoError = telefonoError
        )
    }
}