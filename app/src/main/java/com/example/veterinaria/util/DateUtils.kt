package com.example.veterinaria.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object DateUtils {
    
    private val isoDateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DateFormats.ISO_DATE_TIME)
    private val isoDateFormatter = DateTimeFormatter.ofPattern(Constants.DateFormats.ISO_DATE)
    private val displayDateFormatter = DateTimeFormatter.ofPattern(Constants.DateFormats.DISPLAY_DATE)
    private val displayDateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DateFormats.DISPLAY_DATE_TIME)
    private val displayTimeFormatter = DateTimeFormatter.ofPattern(Constants.DateFormats.DISPLAY_TIME)
    
    /**
     * Convierte una fecha ISO string a formato de visualización
     */
    fun formatDateForDisplay(isoDateString: String?): String {
        if (isoDateString.isNullOrEmpty()) return ""
        
        return try {
            val date = LocalDate.parse(isoDateString, isoDateFormatter)
            date.format(displayDateFormatter)
        } catch (e: DateTimeParseException) {
            try {
                // Intentar con formato de fecha-hora
                val dateTime = LocalDateTime.parse(isoDateString, isoDateTimeFormatter)
                dateTime.format(displayDateFormatter)
            } catch (e: DateTimeParseException) {
                ""
            }
        }
    }
    
    /**
     * Convierte una fecha-hora ISO string a formato de visualización
     */
    fun formatDateTimeForDisplay(isoDateTimeString: String?): String {
        if (isoDateTimeString.isNullOrEmpty()) return ""
        
        return try {
            val dateTime = LocalDateTime.parse(isoDateTimeString, isoDateTimeFormatter)
            dateTime.format(displayDateTimeFormatter)
        } catch (e: DateTimeParseException) {
            ""
        }
    }
    
    /**
     * Convierte una fecha-hora ISO string a formato de hora solamente
     */
    fun formatTimeForDisplay(isoDateTimeString: String?): String {
        if (isoDateTimeString.isNullOrEmpty()) return ""
        
        return try {
            val dateTime = LocalDateTime.parse(isoDateTimeString, isoDateTimeFormatter)
            dateTime.format(displayTimeFormatter)
        } catch (e: DateTimeParseException) {
            ""
        }
    }
    
    /**
     * Convierte una fecha de visualización a formato ISO
     */
    fun formatDateForApi(displayDate: String): String {
        if (displayDate.isEmpty()) return ""
        
        return try {
            val date = LocalDate.parse(displayDate, displayDateFormatter)
            date.format(isoDateFormatter)
        } catch (e: DateTimeParseException) {
            ""
        }
    }
    
    /**
     * Convierte una fecha-hora de visualización a formato ISO
     */
    fun formatDateTimeForApi(displayDateTime: String): String {
        if (displayDateTime.isEmpty()) return ""
        
        return try {
            val dateTime = LocalDateTime.parse(displayDateTime, displayDateTimeFormatter)
            dateTime.format(isoDateTimeFormatter)
        } catch (e: DateTimeParseException) {
            ""
        }
    }
    
    /**
     * Obtiene la fecha actual en formato ISO
     */
    fun getCurrentDateISO(): String {
        return LocalDate.now().format(isoDateFormatter)
    }
    
    /**
     * Obtiene la fecha-hora actual en formato ISO
     */
    fun getCurrentDateTimeISO(): String {
        return LocalDateTime.now().format(isoDateTimeFormatter)
    }
    
    /**
     * Calcula la edad de una mascota basada en su fecha de nacimiento
     */
    fun calculateAge(birthDateISO: String?): Int {
        if (birthDateISO.isNullOrEmpty()) return 0
        
        return try {
            val birthDate = LocalDate.parse(birthDateISO, isoDateFormatter)
            val currentDate = LocalDate.now()
            
            var age = currentDate.year - birthDate.year
            if (currentDate.dayOfYear < birthDate.dayOfYear) {
                age--
            }
            
            if (age < 0) 0 else age
        } catch (e: DateTimeParseException) {
            0
        }
    }
    
    /**
     * Verifica si una fecha es válida
     */
    fun isValidDate(dateString: String, pattern: String = Constants.DateFormats.DISPLAY_DATE): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            LocalDate.parse(dateString, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }
    
    /**
     * Verifica si una fecha-hora es válida
     */
    fun isValidDateTime(dateTimeString: String, pattern: String = Constants.DateFormats.DISPLAY_DATE_TIME): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            LocalDateTime.parse(dateTimeString, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }
    
    /**
     * Compara si una fecha es futura
     */
    fun isFutureDate(dateString: String): Boolean {
        return try {
            val date = LocalDate.parse(dateString, displayDateFormatter)
            date.isAfter(LocalDate.now())
        } catch (e: DateTimeParseException) {
            false
        }
    }
    
    /**
     * Compara si una fecha-hora es futura
     */
    fun isFutureDateTime(dateTimeString: String): Boolean {
        return try {
            val dateTime = LocalDateTime.parse(dateTimeString, displayDateTimeFormatter)
            dateTime.isAfter(LocalDateTime.now())
        } catch (e: DateTimeParseException) {
            false
        }
    }
}