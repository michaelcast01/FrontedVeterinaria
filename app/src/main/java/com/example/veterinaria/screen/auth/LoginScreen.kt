package com.example.veterinaria.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.util.UiState
import com.example.veterinaria.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    authRepository: AuthRepository = AuthRepository(LocalContext.current),
    viewModel: AuthViewModel = viewModel { AuthViewModel(authRepository) }
) {
    val loginState by viewModel.loginState.collectAsState()
    val loginValidation by viewModel.loginValidation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Observar cambios en el estado de login
    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Success -> {
                onLoginSuccess()
                viewModel.clearLoginState()
            }
            is UiState.Error -> {
                showError = true
                errorMessage = (loginState as UiState.Error).message
                viewModel.clearLoginState()
            }
            else -> { /* No hacer nada */ }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo o título
        Text(
            text = "Veterinaria",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 48.dp)
        )
        
        // Formulario de login
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                // Campo usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = viewModel::updateUsername,
                    label = { Text("Usuario") },
                    isError = loginValidation.usernameError != null,
                    supportingText = {
                        loginValidation.usernameError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isLoading
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = viewModel::updatePassword,
                    label = { Text("Contraseña") },
                    isError = loginValidation.passwordError != null,
                    supportingText = {
                        loginValidation.passwordError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (passwordVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isLoading
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón de login
                com.example.veterinaria.ui.components.VetButton(
                    text = "Iniciar Sesión",
                    onClick = { viewModel.login() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && loginValidation.isValid,
                    loading = isLoading
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón de registro
                TextButton(
                    onClick = onNavigateToRegister,
                    enabled = !isLoading
                ) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }
        }
        
        // Mostrar error si existe
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
    }
}