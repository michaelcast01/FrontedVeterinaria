package com.example.veterinaria.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.data.repository.ClienteRepository
import com.example.veterinaria.data.repository.MascotaRepository
import com.example.veterinaria.util.UiState
import com.example.veterinaria.viewmodel.AuthViewModel
import com.example.veterinaria.viewmodel.ClienteViewModel
import com.example.veterinaria.viewmodel.MascotaViewModel

data class DashboardCard(
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: androidx.compose.ui.graphics.Color,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToClientes: () -> Unit,
    onNavigateToMascotas: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val clienteRepository = remember { ClienteRepository(authRepository) }
    val mascotaRepository = remember { MascotaRepository(authRepository) }
    
    val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository) }
    val clienteViewModel: ClienteViewModel = viewModel { ClienteViewModel(clienteRepository, authRepository) }
    val mascotaViewModel: MascotaViewModel = viewModel { MascotaViewModel(mascotaRepository, authRepository) }
    
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)
    val estadisticasClientes by clienteViewModel.estadisticasState.collectAsState(initial = UiState.Loading)
    val estadisticasMascotas by mascotaViewModel.estadisticasMascotas.collectAsState(initial = UiState.Loading)
    
    val dashboardCards = listOf(
        DashboardCard(
            title = "Clientes",
            subtitle = when (val state = estadisticasClientes) {
                is UiState.Success -> "${state.data.totalClientesActivos} activos"
                else -> "Gestionar clientes"
            },
            icon = Icons.Default.People,
            color = MaterialTheme.colorScheme.primary,
            onClick = onNavigateToClientes
        ),
        DashboardCard(
            title = "Mascotas",
            subtitle = when (val state = estadisticasMascotas) {
                is UiState.Success -> "${state.data.totalMascotasActivas} activas"
                else -> "Gestionar mascotas"
            },
            icon = Icons.Default.Pets,
            color = MaterialTheme.colorScheme.secondary,
            onClick = onNavigateToMascotas
        ),
        DashboardCard(
            title = "Citas",
            subtitle = "Programar citas",
            icon = Icons.Default.Schedule,
            color = MaterialTheme.colorScheme.tertiary,
            onClick = onNavigateToCitas
        )
    )
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // App Bar
        TopAppBar(
            title = { 
                Text(
                    text = "Veterinaria",
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = onLogout) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Bienvenida
            currentUser?.let { user ->
                Text(
                    text = "Bienvenido, ${user.username}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Tipo: ${user.tipoUsuario}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            
            // Título de sección
            Text(
                text = "Opciones principales",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Cards horizontales
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(dashboardCards) { card ->
                    DashboardCardItem(card = card)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sección de estadísticas rápidas
            Text(
                text = "Resumen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Estadística clientes
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        when (val state = estadisticasClientes) {
                            is UiState.Success -> {
                                Text(
                                    text = "${state.data.totalClientesActivos}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Clientes Activos",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            is UiState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            is UiState.Error -> {
                                Text(
                                    text = "Error",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
                
                // Estadística mascotas
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Pets,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        when (val state = estadisticasMascotas) {
                            is UiState.Success -> {
                                Text(
                                    text = "${state.data.totalMascotasActivas}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = "Mascotas Activas",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            is UiState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            is UiState.Error -> {
                                Text(
                                    text = "Error",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardCardItem(card: DashboardCard) {
    Card(
        onClick = card.onClick,
        modifier = Modifier
            .width(200.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = card.color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = card.icon,
                contentDescription = null,
                tint = card.color,
                modifier = Modifier.size(40.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = card.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = card.color
            )
            
            Text(
                text = card.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}