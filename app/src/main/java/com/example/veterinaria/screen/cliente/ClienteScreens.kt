package com.example.veterinaria.screen.cliente

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
fun ClientesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Clientes") },
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
                    text = "Pantalla de Clientes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aquí se mostrará la lista de clientes",
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = onNavigateToCreate) {
                    Text("Crear Cliente")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteDetailScreen(
    clienteId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToMascotas: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detalle Cliente") },
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
                    text = "Detalle del Cliente #$clienteId",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onNavigateToEdit(clienteId) }) {
                    Text("Editar Cliente")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onNavigateToMascotas(clienteId) }) {
                    Text("Ver Mascotas")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteCreateEditScreen(
    clienteId: Long?,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val isEditing = clienteId != null
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(if (isEditing) "Editar Cliente" else "Crear Cliente") },
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
                    text = if (isEditing) "Editar Cliente #$clienteId" else "Crear Nuevo Cliente",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Formulario de cliente aquí",
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = onSaveSuccess) {
                    Text("Guardar")
                }
            }
        }
    }
}