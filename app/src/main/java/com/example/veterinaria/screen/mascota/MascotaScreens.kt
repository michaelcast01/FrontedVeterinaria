package com.example.veterinaria.screen.mascota

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Mascotas") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Pantalla de Mascotas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aquí se mostrará la lista de mascotas",
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = onNavigateToCreate) {
                    Text("Crear Mascota")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaDetailScreen(
    mascotaId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToCitas: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detalle Mascota") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Detalle de la Mascota #$mascotaId",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onNavigateToEdit(mascotaId) }) {
                    Text("Editar Mascota")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onNavigateToCitas(mascotaId) }) {
                    Text("Ver Citas")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaCreateEditScreen(
    mascotaId: Long?,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val isEditing = mascotaId != null
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(if (isEditing) "Editar Mascota" else "Crear Mascota") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isEditing) "Editar Mascota #$mascotaId" else "Crear Nueva Mascota",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Formulario de mascota aquí",
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = onSaveSuccess) {
                    Text("Guardar")
                }
            }
        }
    }
}