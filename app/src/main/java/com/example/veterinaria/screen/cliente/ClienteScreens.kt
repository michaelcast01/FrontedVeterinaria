package com.example.veterinaria.screen.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import com.example.veterinaria.viewmodel.ClienteViewModel
import com.example.veterinaria.util.UiState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    clienteViewModel: ClienteViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    val vm = clienteViewModel
    val clientesState by vm.clientesState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Clientes") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        when (clientesState) {
            is UiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text((clientesState as UiState.Error).message) }
            is UiState.Success -> {
                val list = (clientesState as UiState.Success<List<com.example.veterinaria.data.model.ClienteDTO>>).data
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    list.forEach { cliente ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = cliente.nombre ?: "-", fontWeight = FontWeight.Bold)
                                    Text(text = cliente.apellido ?: "-", style = MaterialTheme.typography.bodySmall)
                                }
                                Button(onClick = { onNavigateToDetail(cliente.id ?: 0L) }) { Text("Ver") }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = onNavigateToCreate, modifier = Modifier.fillMaxWidth()) { Text("Crear Cliente") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteDetailScreen(
    clienteViewModel: ClienteViewModel,
    clienteId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToMascotas: (Long) -> Unit
) {
    val vm = clienteViewModel
    val detalleState by vm.clienteDetailState.collectAsState()

    LaunchedEffect(clienteId) { vm.loadClienteById(clienteId) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detalle Cliente") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        when (detalleState) {
            is UiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text((detalleState as UiState.Error).message) }
            is UiState.Success -> {
                val cliente = (detalleState as UiState.Success<com.example.veterinaria.data.model.ClienteDTO>).data
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(text = cliente.nombre ?: "-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Apellido: ${cliente.apellido}")
                    cliente.telefono?.let { Text(text = "Teléfono: $it") }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onNavigateToEdit(clienteId) }) { Text("Editar Cliente") }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onNavigateToMascotas(clienteId) }) { Text("Ver Mascotas") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteCreateEditScreen(
    clienteViewModel: ClienteViewModel,
    clienteId: Long?,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val isEditing = clienteId != null
    val vm = clienteViewModel
    val formState by vm.clienteForm.collectAsState()
    val formValidation by vm.formValidation.collectAsState()
    val saveState by vm.saveClienteState.collectAsState()

    LaunchedEffect(Unit) {
        if (isEditing && clienteId != null) vm.loadClienteById(clienteId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(if (isEditing) "Editar Cliente" else "Crear Cliente") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") }
            }
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(value = formState.nombre, onValueChange = { vm.updateClienteForm(nombre = it) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = formState.apellido, onValueChange = { vm.updateClienteForm(apellido = it) }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            com.example.veterinaria.ui.components.VetButton(text = if (isEditing) "Actualizar" else "Crear", onClick = { vm.saveCliente(clienteId) }, modifier = Modifier.fillMaxWidth(), enabled = formValidation.isValid)

            Spacer(modifier = Modifier.height(8.dp))
            when (saveState) {
                is UiState.Loading -> { }
                is UiState.Error -> Text(text = (saveState as UiState.Error).message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> LaunchedEffect(saveState) { onSaveSuccess() }
            }
        }
    }
}