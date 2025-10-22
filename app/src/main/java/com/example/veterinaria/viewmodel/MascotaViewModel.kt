package com.example.veterinaria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaria.data.model.MascotaDTO
import com.example.veterinaria.data.model.EstadisticasMascotasResponse
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.data.repository.MascotaRepository
import com.example.veterinaria.util.Debouncer
import com.example.veterinaria.util.UiState
import com.example.veterinaria.util.ValidationUtils
import com.example.veterinaria.util.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MascotaViewModel(
    private val mascotaRepository: MascotaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _mascotasState = MutableStateFlow<UiState<List<MascotaDTO>>>(UiState.Loading)
    val mascotasState: StateFlow<UiState<List<MascotaDTO>>> = _mascotasState.asStateFlow()
    
    private val _mascotaDetailState = MutableStateFlow<UiState<MascotaDTO>>(UiState.Loading)
    val mascotaDetailState: StateFlow<UiState<MascotaDTO>> = _mascotaDetailState.asStateFlow()
    
    private val _saveMascotaState = MutableStateFlow<UiState<MascotaDTO>>(UiState.Loading)
    val saveMascotaState: StateFlow<UiState<MascotaDTO>> = _saveMascotaState.asStateFlow()
    
    private val _especiesState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val especiesState: StateFlow<UiState<List<String>>> = _especiesState.asStateFlow()
    
    private val _razasState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val razasState: StateFlow<UiState<List<String>>> = _razasState.asStateFlow()
    
    private val _estadisticasState = MutableStateFlow<UiState<EstadisticasMascotasResponse>>(UiState.Loading)
    val estadisticasMascotas: StateFlow<UiState<EstadisticasMascotasResponse>> = _estadisticasState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Estados del formulario
    private val _mascotaForm = MutableStateFlow(MascotaFormState())
    val mascotaForm: StateFlow<MascotaFormState> = _mascotaForm.asStateFlow()
    
    private val _formValidation = MutableStateFlow(ValidationUtils.MascotaValidation(true))
    val formValidation: StateFlow<ValidationUtils.MascotaValidation> = _formValidation.asStateFlow()
    
    private val searchDebouncer = Debouncer(300L)
    
    data class MascotaFormState(
        val nombre: String = "",
        val especie: String = "",
        val raza: String = "",
        val sexo: String = "",
        val fechaNacimiento: String = "",
        val peso: String = "",
        val color: String = "",
        val observaciones: String = "",
        val clienteId: Long = 0L,
        val activo: Boolean = true
    )
    
    init {
        loadMascotas()
        loadEspecies()
        loadEstadisticas()
    }
    
    fun loadMascotas() {
        viewModelScope.launch {
            _isLoading.value = true
            
            mascotaRepository.getMascotasActivas().collect { result ->
                _mascotasState.value = result.toUiState()
                _isLoading.value = false
            }
        }
    }
    
    fun loadMascotaById(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = mascotaRepository.getMascotaById(id)
            _mascotaDetailState.value = result.toUiState()
            
            if (result.isSuccess) {
                val mascota = result.getOrNull()!!
                updateMascotaForm(
                    nombre = mascota.nombre,
                    especie = mascota.especie,
                    raza = mascota.raza ?: "",
                    sexo = mascota.sexo ?: "",
                    fechaNacimiento = mascota.fechaNacimiento ?: "",
                    peso = mascota.peso?.toString() ?: "",
                    color = mascota.color ?: "",
                    observaciones = mascota.observaciones ?: "",
                    clienteId = mascota.clienteId,
                    activo = mascota.activo
                )
            }
            
            _isLoading.value = false
        }
    }
    
    fun loadEspecies() {
        viewModelScope.launch {
            val result = mascotaRepository.getEspecies()
            _especiesState.value = result.toUiState()
        }
    }
    
    fun loadRazasByEspecie(especie: String) {
        if (especie.isEmpty()) return
        
        viewModelScope.launch {
            val result = mascotaRepository.getRazasByEspecie(especie)
            _razasState.value = result.toUiState()
        }
    }
    
    fun updateMascotaForm(
        nombre: String? = null,
        especie: String? = null,
        raza: String? = null,
        sexo: String? = null,
        fechaNacimiento: String? = null,
        peso: String? = null,
        color: String? = null,
        observaciones: String? = null,
        clienteId: Long? = null,
        activo: Boolean? = null
    ) {
        _mascotaForm.value = _mascotaForm.value.copy(
            nombre = nombre ?: _mascotaForm.value.nombre,
            especie = especie ?: _mascotaForm.value.especie,
            raza = raza ?: _mascotaForm.value.raza,
            sexo = sexo ?: _mascotaForm.value.sexo,
            fechaNacimiento = fechaNacimiento ?: _mascotaForm.value.fechaNacimiento,
            peso = peso ?: _mascotaForm.value.peso,
            color = color ?: _mascotaForm.value.color,
            observaciones = observaciones ?: _mascotaForm.value.observaciones,
            clienteId = clienteId ?: _mascotaForm.value.clienteId,
            activo = activo ?: _mascotaForm.value.activo
        )
        
        // Cargar razas cuando cambie la especie
        if (especie != null && especie != _mascotaForm.value.especie) {
            loadRazasByEspecie(especie)
        }
        
        validateForm()
    }
    
    private fun validateForm() {
        val form = _mascotaForm.value
        val pesoValue = form.peso.toDoubleOrNull()
        
        _formValidation.value = ValidationUtils.validateMascota(
            nombre = form.nombre,
            especie = form.especie,
            peso = pesoValue,
            fechaNacimiento = form.fechaNacimiento,
            observaciones = form.observaciones
        )
    }
    
    fun saveMascota(mascotaId: Long? = null) {
        if (!_formValidation.value.isValid) return
        
        viewModelScope.launch {
            _isLoading.value = true
            
            val form = _mascotaForm.value
            val mascotaDto = MascotaDTO(
                id = mascotaId,
                nombre = form.nombre,
                especie = form.especie,
                raza = form.raza.ifEmpty { null },
                sexo = form.sexo.ifEmpty { null },
                fechaNacimiento = form.fechaNacimiento.ifEmpty { null },
                peso = form.peso.toDoubleOrNull(),
                color = form.color.ifEmpty { null },
                observaciones = form.observaciones.ifEmpty { null },
                clienteId = form.clienteId,
                activo = form.activo
            )
            
            val result = if (mascotaId == null) {
                mascotaRepository.createMascota(mascotaDto)
            } else {
                mascotaRepository.updateMascota(mascotaId, mascotaDto)
            }
            
            _saveMascotaState.value = result.toUiState()
            _isLoading.value = false
            
            if (result.isSuccess) {
                clearForm()
                loadMascotas()
            }
        }
    }
    
    fun clearForm() {
        _mascotaForm.value = MascotaFormState()
        _formValidation.value = ValidationUtils.MascotaValidation(true)
    }
    
    fun clearSaveState() {
        _saveMascotaState.value = UiState.Loading
    }
    
    fun loadEstadisticas() {
        viewModelScope.launch {
            val result = mascotaRepository.getEstadisticasMascotas()
            _estadisticasState.value = result.toUiState()
        }
    }
}