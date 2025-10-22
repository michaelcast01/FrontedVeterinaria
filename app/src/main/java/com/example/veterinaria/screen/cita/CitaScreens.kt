package com.example.veterinaria.screen.cita

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
fun CitasScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Citas") },
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
                    text = "Pantalla de Citas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aquí se mostrará la lista de citas",
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = onNavigateToCreate) {
                    Text("Crear Cita")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaDetailScreen(
    citaId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detalle Cita") },
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
                    text = "Detalle de la Cita #$citaId",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onNavigateToEdit(citaId) }) {
                    Text("Editar Cita")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaCreateEditScreen(
    citaId: Long?,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val isEditing = citaId != null
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(if (isEditing) "Editar Cita" else "Crear Cita") },
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
                    text = if (isEditing) "Editar Cita #$citaId" else "Crear Nueva Cita",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Formulario de cita aquí",
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = onSaveSuccess) {
                    Text("Guardar")
                }
            }
        }
    }
}