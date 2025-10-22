package com.example.veterinaria.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.util.Constants
import com.example.veterinaria.util.UiState
import com.example.veterinaria.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    authRepository: AuthRepository = AuthRepository(LocalContext.current),
    viewModel: AuthViewModel = viewModel { AuthViewModel(authRepository) }
) {
    val registerState by viewModel.registerState.collectAsState()
    val registerValidation by viewModel.registerValidation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val username by viewModel.registerUsername.collectAsState()
    val email by viewModel.registerEmail.collectAsState()
    val password by viewModel.registerPassword.collectAsState()
    val nombre by viewModel.registerNombre.collectAsState()
    val apellido by viewModel.registerApellido.collectAsState()
    val telefono by viewModel.registerTelefono.collectAsState()
    val tipoUsuario by viewModel.registerTipoUsuario.collectAsState()
    
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    
    // Observar cambios en el estado de registro
    LaunchedEffect(registerState) {
        when (registerState) {
            is UiState.Success -> {
                showSuccess = true
                viewModel.clearRegisterState()
            }
            is UiState.Error -> {
                showError = true
                errorMessage = (registerState as UiState.Error).message
                viewModel.clearRegisterState()
            }
            else -> { /* No hacer nada */ }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // App Bar
        TopAppBar(
            title = { Text("Registro") },
            navigationIcon = {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )
        
        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Nueva Cuenta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Usuario
                    OutlinedTextField(
                        value = username,
                        onValueChange = viewModel::updateRegisterUsername,
                        label = { Text("Usuario") },
                        isError = registerValidation.usernameError != null,
                        supportingText = {
                            registerValidation.usernameError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = viewModel::updateRegisterEmail,
                        label = { Text("Email") },
                        isError = registerValidation.emailError != null,
                        supportingText = {
                            registerValidation.emailError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = viewModel::updateRegisterPassword,
                        label = { Text("Contraseña") },
                        isError = registerValidation.passwordError != null,
                        supportingText = {
                            registerValidation.passwordError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = viewModel::updateRegisterNombre,
                        label = { Text("Nombre") },
                        isError = registerValidation.nombreError != null,
                        supportingText = {
                            registerValidation.nombreError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Apellido
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = viewModel::updateRegisterApellido,
                        label = { Text("Apellido") },
                        isError = registerValidation.apellidoError != null,
                        supportingText = {
                            registerValidation.apellidoError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Teléfono
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = viewModel::updateRegisterTelefono,
                        label = { Text("Teléfono") },
                        isError = registerValidation.telefonoError != null,
                        supportingText = {
                            registerValidation.telefonoError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Tipo de usuario
                    var expandedTipoUsuario by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedTipoUsuario,
                        onExpandedChange = { expandedTipoUsuario = !expandedTipoUsuario }
                    ) {
                        OutlinedTextField(
                            value = Constants.Estados.TIPOS_USUARIO.find { it.first == tipoUsuario }?.second ?: tipoUsuario,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Tipo de Usuario") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoUsuario) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            enabled = !isLoading
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expandedTipoUsuario,
                            onDismissRequest = { expandedTipoUsuario = false }
                        ) {
                            Constants.Estados.TIPOS_USUARIO.forEach { (valor, descripcion) ->
                                DropdownMenuItem(
                                    text = { Text(descripcion) },
                                    onClick = {
                                        viewModel.updateRegisterTipoUsuario(valor)
                                        expandedTipoUsuario = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botón de registro
                    com.example.veterinaria.ui.components.VetButton(
                        text = "Registrarse",
                        onClick = { viewModel.register() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && registerValidation.isValid,
                        loading = isLoading
                    )
                }
            }
            
            // Mostrar mensajes
            if (showError) {
                LaunchedEffect(showError) {
                    kotlinx.coroutines.delay(3000)
                    showError = false
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            if (showSuccess) {
                LaunchedEffect(showSuccess) {
                    kotlinx.coroutines.delay(2000)
                    onRegisterSuccess()
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Registro exitoso. Redirigiendo al login...",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}