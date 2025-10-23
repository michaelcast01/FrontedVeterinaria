package com.example.veterinaria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaria.data.model.CitaDTO
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.data.repository.CitaRepository
import com.example.veterinaria.util.UiState
import com.example.veterinaria.util.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.format.DateTimeFormatter

class CitaViewModel(
    private val citaRepository: CitaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _citasState = MutableStateFlow<UiState<List<CitaDTO>>>(UiState.Loading)
    val citasState: StateFlow<UiState<List<CitaDTO>>> = _citasState.asStateFlow()

    private val _citaDetailState = MutableStateFlow<UiState<CitaDTO>>(UiState.Loading)
    val citaDetailState: StateFlow<UiState<CitaDTO>> = _citaDetailState.asStateFlow()

    private val _saveCitaState = MutableStateFlow<UiState<CitaDTO>>(UiState.Loading)
    val saveCitaState: StateFlow<UiState<CitaDTO>> = _saveCitaState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCitas()
    }

    fun loadCitas() {
        viewModelScope.launch {
            _isLoading.value = true
            citaRepository.getAllCitas().collect { result ->
                _citasState.value = result.toUiState()
                _isLoading.value = false
            }
        }
    }

    suspend fun loadCitaById(id: Long) {
        _isLoading.value = true
        val result = citaRepository.getCitaById(id)
        _citaDetailState.value = result.toUiState()
        _isLoading.value = false
    }

    fun createOrUpdateCita(citaId: Long?, cita: CitaDTO) {
        viewModelScope.launch {
            _isLoading.value = true
            // If clienteId is 0, try to use current logged user id
            val clienteId = if (cita.clienteId == 0L) {
                try { authRepository.currentUser.first()?.id ?: 0L } catch (e: Exception) { 0L }
            } else cita.clienteId

            // If fechaHora is blank, set to now (ISO-8601)
            val fecha = if (cita.fechaHora.isBlank()) {
                DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            } else cita.fechaHora

            val citaToSend = cita.copy(clienteId = clienteId, fechaHora = fecha)

            val result = if (citaId == null) citaRepository.createCita(citaToSend) else citaRepository.updateCita(citaId, citaToSend)
            _saveCitaState.value = result.toUiState()
            _isLoading.value = false
            if (result.isSuccess) loadCitas()
        }
    }
}
