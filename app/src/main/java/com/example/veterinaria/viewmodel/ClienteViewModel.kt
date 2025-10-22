package com.example.veterinaria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaria.data.model.ClienteDTO
import com.example.veterinaria.data.model.EstadisticasClientesResponse
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.data.repository.ClienteRepository
import com.example.veterinaria.util.Debouncer
import com.example.veterinaria.util.UiState
import com.example.veterinaria.util.ValidationUtils
import com.example.veterinaria.util.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClienteViewModel(
    private val clienteRepository: ClienteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _clientesState = MutableStateFlow<UiState<List<ClienteDTO>>>(UiState.Loading)
    val clientesState: StateFlow<UiState<List<ClienteDTO>>> = _clientesState.asStateFlow()
    
    private val _clienteDetailState = MutableStateFlow<UiState<ClienteDTO>>(UiState.Loading)
    val clienteDetailState: StateFlow<UiState<ClienteDTO>> = _clienteDetailState.asStateFlow()
    
    private val _saveClienteState = MutableStateFlow<UiState<ClienteDTO>>(UiState.Loading)
    val saveClienteState: StateFlow<UiState<ClienteDTO>> = _saveClienteState.asStateFlow()
    
    private val _deleteClienteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteClienteState: StateFlow<UiState<Unit>> = _deleteClienteState.asStateFlow()
    
    private val _estadisticasState = MutableStateFlow<UiState<EstadisticasClientesResponse>>(UiState.Loading)
    val estadisticasState: StateFlow<UiState<EstadisticasClientesResponse>> = _estadisticasState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _showOnlyActivos = MutableStateFlow(true)
    val showOnlyActivos: StateFlow<Boolean> = _showOnlyActivos.asStateFlow()
    
    // Estados del formulario
    private val _clienteForm = MutableStateFlow(ClienteFormState())
    val clienteForm: StateFlow<ClienteFormState> = _clienteForm.asStateFlow()
    
    private val _formValidation = MutableStateFlow(ValidationUtils.ClienteValidation(true))
    val formValidation: StateFlow<ValidationUtils.ClienteValidation> = _formValidation.asStateFlow()
    
    private val searchDebouncer = Debouncer(300L)
    
    data class ClienteFormState(
        val nombre: String = "",
        val apellido: String = "",
        val documento: String = "",
        val telefono: String = "",
        val email: String = "",
        val direccion: String = "",
        val activo: Boolean = true
    )
    
    init {
        loadClientes()
        loadEstadisticas()
    }
    
    fun loadClientes() {
        viewModelScope.launch {
            _isLoading.value = true
            
            val flow = if (_showOnlyActivos.value) {
                clienteRepository.getClientesActivos()
            } else {
                clienteRepository.getAllClientes()
            }
            
            flow.collect { result ->
                _clientesState.value = result.toUiState()
                _isLoading.value = false
            }
        }
    }
    
    fun loadClienteById(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = clienteRepository.getClienteById(id)
            _clienteDetailState.value = result.toUiState()
            
            // Cargar datos en el formulario para edición
            if (result.isSuccess) {
                val cliente = result.getOrNull()!!
                updateClienteForm(
                    nombre = cliente.nombre,
                    apellido = cliente.apellido,
                    documento = cliente.documento ?: "",
                    telefono = cliente.telefono ?: "",
                    email = cliente.email ?: "",
                    direccion = cliente.direccion ?: "",
                    activo = cliente.activo
                )
            }
            
            _isLoading.value = false
        }
    }
    
    fun searchClientes(query: String) {
        _searchQuery.value = query
        
        if (query.isEmpty()) {
            loadClientes()
            return
        }
        
        searchDebouncer.debounce(viewModelScope) {
            _isLoading.value = true
            
            clienteRepository.buscarClientes(query).collect { result ->
                _clientesState.value = result.toUiState()
                _isLoading.value = false
            }
        }
    }
    
    fun toggleShowOnlyActivos() {
        _showOnlyActivos.value = !_showOnlyActivos.value
        loadClientes()
    }
    
    fun updateClienteForm(
        nombre: String? = null,
        apellido: String? = null,
        documento: String? = null,
        telefono: String? = null,
        email: String? = null,
        direccion: String? = null,
        activo: Boolean? = null
    ) {
        _clienteForm.value = _clienteForm.value.copy(
            nombre = nombre ?: _clienteForm.value.nombre,
            apellido = apellido ?: _clienteForm.value.apellido,
            documento = documento ?: _clienteForm.value.documento,
            telefono = telefono ?: _clienteForm.value.telefono,
            email = email ?: _clienteForm.value.email,
            direccion = direccion ?: _clienteForm.value.direccion,
            activo = activo ?: _clienteForm.value.activo
        )
        validateForm()
    }
    
    private fun validateForm() {
        val form = _clienteForm.value
        _formValidation.value = ValidationUtils.validateCliente(
            nombre = form.nombre,
            apellido = form.apellido,
            documento = form.documento,
            telefono = form.telefono,
            email = form.email,
            direccion = form.direccion
        )
    }
    
    fun saveCliente(clienteId: Long? = null) {
        if (!_formValidation.value.isValid) return
        
        viewModelScope.launch {
            _isLoading.value = true
            
            val form = _clienteForm.value
            val clienteDto = ClienteDTO(
                id = clienteId,
                nombre = form.nombre,
                apellido = form.apellido,
                documento = form.documento.ifEmpty { null },
                telefono = form.telefono.ifEmpty { null },
                email = form.email.ifEmpty { null },
                direccion = form.direccion.ifEmpty { null },
                activo = form.activo
            )
            
            val result = if (clienteId == null) {
                clienteRepository.createCliente(clienteDto)
            } else {
                clienteRepository.updateCliente(clienteId, clienteDto)
            }
            
            _saveClienteState.value = result.toUiState()
            _isLoading.value = false
            
            if (result.isSuccess) {
                clearForm()
                loadClientes()
                loadEstadisticas()
            }
        }
    }
    
    fun deleteCliente(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = clienteRepository.deleteCliente(id)
            _deleteClienteState.value = result.toUiState()
            _isLoading.value = false
            
            if (result.isSuccess) {
                loadClientes()
                loadEstadisticas()
            }
        }
    }
    
    fun activarCliente(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = clienteRepository.activarCliente(id)
            _isLoading.value = false
            
            if (result.isSuccess) {
                loadClientes()
                loadEstadisticas()
            }
        }
    }
    
    fun loadEstadisticas() {
        viewModelScope.launch {
            val result = clienteRepository.getEstadisticasClientes()
            _estadisticasState.value = result.toUiState()
        }
    }
    
    fun clearForm() {
        _clienteForm.value = ClienteFormState()
        _formValidation.value = ValidationUtils.ClienteValidation(true)
    }
    
    fun clearSaveState() {
        _saveClienteState.value = UiState.Loading
    }
    
    fun clearDeleteState() {
        _deleteClienteState.value = UiState.Loading
    }
}