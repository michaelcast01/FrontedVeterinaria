package com.example.veterinaria.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Clase para manejar eventos de UI de una sola vez (como navegación, mostrar snackbar, etc.)
 */
class UiEvent<out T>(private val content: T) {
    
    private var hasBeenHandled = false
    
    /**
     * Retorna el contenido y previene su uso nuevamente.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    
    /**
     * Retorna el contenido, incluso si ya ha sido manejado.
     */
    fun peekContent(): T = content
}

/**
 * Canal para eventos de UI de una sola vez
 */
class UiEventChannel<T> {
    private val _events = Channel<UiEvent<T>>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    
    fun sendEvent(event: T) {
        _events.trySend(UiEvent(event))
    }
}

/**
 * Utilidades para formateo de texto
 */
object TextUtils {
    
    /**
     * Capitaliza la primera letra de cada palabra
     */
    fun capitalizeWords(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
    
    /**
     * Formatea un teléfono para visualización
     */
    fun formatPhone(phone: String): String {
        if (phone.isEmpty()) return ""
        
        return when {
            phone.startsWith("+") -> phone
            phone.length == 10 -> "${phone.substring(0, 3)}-${phone.substring(3, 6)}-${phone.substring(6)}"
            phone.length == 8 -> "${phone.substring(0, 4)}-${phone.substring(4)}"
            else -> phone
        }
    }
    
    /**
     * Trunca un texto si es muy largo
     */
    fun truncate(text: String, maxLength: Int): String {
        return if (text.length <= maxLength) {
            text
        } else {
            "${text.substring(0, maxLength - 3)}..."
        }
    }
    
    /**
     * Formatea un precio/costo
     */
    fun formatCurrency(amount: Double): String {
        return "$%.2f".format(amount)
    }
    
    /**
     * Extrae las iniciales de un nombre completo
     */
    fun getInitials(fullName: String): String {
        return fullName.split(" ")
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .take(2)
            .joinToString("")
    }
}

/**
 * Utilidades para el estado de carga/error
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

/**
 * Extensión para manejar resultados de forma segura
 */
fun <T> Result<T>.toUiState(): UiState<T> {
    return fold(
        onSuccess = { UiState.Success(it) },
        onFailure = { UiState.Error(it.message ?: Constants.ErrorMessages.UNKNOWN_ERROR) }
    )
}

/**
 * Hook para observar el ciclo de vida
 */
@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = remember { LifecycleEventObserver(onEvent) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(eventHandler)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(eventHandler)
        }
    }
}

/**
 * Debounce para búsquedas
 */
class Debouncer(private val delayMs: Long = 300L) {
    private var debounceJob: kotlinx.coroutines.Job? = null
    
    fun debounce(
        scope: kotlinx.coroutines.CoroutineScope,
        action: suspend () -> Unit
    ) {
        debounceJob?.cancel()
        debounceJob = scope.launch {
            kotlinx.coroutines.delay(delayMs)
            action()
        }
    }
}

/**
 * Extensiones útiles
 */
fun String?.orEmpty(): String = this ?: ""

fun String?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

fun Double?.orZero(): Double = this ?: 0.0

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0L