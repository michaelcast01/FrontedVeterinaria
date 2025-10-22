package com.example.veterinaria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaria.data.model.LoginDTO
import com.example.veterinaria.data.model.LoginResponse
import com.example.veterinaria.data.model.RegisterDTO
import com.example.veterinaria.data.model.RegisterResponse
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.util.UiState
import com.example.veterinaria.util.ValidationUtils
import com.example.veterinaria.util.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    
    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Loading)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState.asStateFlow()
    
    private val _registerState = MutableStateFlow<UiState<RegisterResponse>>(UiState.Loading)
    val registerState: StateFlow<UiState<RegisterResponse>> = _registerState.asStateFlow()
    
    private val _loginValidation = MutableStateFlow(ValidationUtils.LoginValidation(true))
    val loginValidation: StateFlow<ValidationUtils.LoginValidation> = _loginValidation.asStateFlow()
    
    private val _registerValidation = MutableStateFlow(ValidationUtils.RegisterValidation(true))
    val registerValidation: StateFlow<ValidationUtils.RegisterValidation> = _registerValidation.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Estados del formulario de login
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    
    // Estados del formulario de registro
    private val _registerUsername = MutableStateFlow("")
    val registerUsername: StateFlow<String> = _registerUsername.asStateFlow()
    
    private val _registerEmail = MutableStateFlow("")
    val registerEmail: StateFlow<String> = _registerEmail.asStateFlow()
    
    private val _registerPassword = MutableStateFlow("")
    val registerPassword: StateFlow<String> = _registerPassword.asStateFlow()
    
    private val _registerNombre = MutableStateFlow("")
    val registerNombre: StateFlow<String> = _registerNombre.asStateFlow()
    
    private val _registerApellido = MutableStateFlow("")
    val registerApellido: StateFlow<String> = _registerApellido.asStateFlow()
    
    private val _registerTelefono = MutableStateFlow("")
    val registerTelefono: StateFlow<String> = _registerTelefono.asStateFlow()
    
    private val _registerTipoUsuario = MutableStateFlow("CLIENTE")
    val registerTipoUsuario: StateFlow<String> = _registerTipoUsuario.asStateFlow()
    
    // Observar estado de autenticación
    val isLoggedIn = authRepository.isLoggedIn
    val currentUser = authRepository.currentUser
    
    fun updateUsername(value: String) {
        _username.value = value
        validateLogin()
    }
    
    fun updatePassword(value: String) {
        _password.value = value
        validateLogin()
    }
    
    fun updateRegisterUsername(value: String) {
        _registerUsername.value = value
        validateRegister()
    }
    
    fun updateRegisterEmail(value: String) {
        _registerEmail.value = value
        validateRegister()
    }
    
    fun updateRegisterPassword(value: String) {
        _registerPassword.value = value
        validateRegister()
    }
    
    fun updateRegisterNombre(value: String) {
        _registerNombre.value = value
        validateRegister()
    }
    
    fun updateRegisterApellido(value: String) {
        _registerApellido.value = value
        validateRegister()
    }
    
    fun updateRegisterTelefono(value: String) {
        _registerTelefono.value = value
        validateRegister()
    }
    
    fun updateRegisterTipoUsuario(value: String) {
        _registerTipoUsuario.value = value
    }
    
    private fun validateLogin() {
        _loginValidation.value = ValidationUtils.validateLogin(
            username = _username.value,
            password = _password.value
        )
    }
    
    private fun validateRegister() {
        _registerValidation.value = ValidationUtils.validateRegister(
            username = _registerUsername.value,
            email = _registerEmail.value,
            password = _registerPassword.value,
            nombre = _registerNombre.value,
            apellido = _registerApellido.value,
            telefono = _registerTelefono.value
        )
    }
    
    fun login() {
        if (!_loginValidation.value.isValid) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = UiState.Loading
            
            val result = authRepository.login(
                username = _username.value,
                password = _password.value
            )
            
            _loginState.value = result.toUiState()
            _isLoading.value = false
            
            // Limpiar formulario en caso de éxito
            if (result.isSuccess) {
                clearLoginForm()
            }
        }
    }
    
    fun register() {
        if (!_registerValidation.value.isValid) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _registerState.value = UiState.Loading
            
            val registerData = RegisterDTO(
                username = _registerUsername.value,
                email = _registerEmail.value,
                password = _registerPassword.value,
                nombre = _registerNombre.value,
                apellido = _registerApellido.value,
                telefono = _registerTelefono.value,
                tipoUsuario = _registerTipoUsuario.value
            )
            
            val result = authRepository.register(registerData)
            
            _registerState.value = result.toUiState()
            _isLoading.value = false
            
            // Limpiar formulario en caso de éxito
            if (result.isSuccess) {
                clearRegisterForm()
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            clearLoginForm()
            clearRegisterForm()
        }
    }
    
    private fun clearLoginForm() {
        _username.value = ""
        _password.value = ""
        _loginValidation.value = ValidationUtils.LoginValidation(true)
    }
    
    private fun clearRegisterForm() {
        _registerUsername.value = ""
        _registerEmail.value = ""
        _registerPassword.value = ""
        _registerNombre.value = ""
        _registerApellido.value = ""
        _registerTelefono.value = ""
    _registerTipoUsuario.value = "CLIENTE"
        _registerValidation.value = ValidationUtils.RegisterValidation(true)
    }
    
    fun clearLoginState() {
        _loginState.value = UiState.Loading
    }
    
    fun clearRegisterState() {
        _registerState.value = UiState.Loading
    }
}