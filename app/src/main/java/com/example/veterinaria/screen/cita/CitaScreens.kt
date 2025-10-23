package com.example.veterinaria.screen.cita

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.veterinaria.viewmodel.CitaViewModel
import com.example.veterinaria.util.UiState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(
    citaViewModel: CitaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    val vm = citaViewModel
    val citasState by vm.citasState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Citas") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") }
            }
        )

        when (citasState) {
            is UiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text((citasState as UiState.Error).message) }
            is UiState.Success -> {
                val list = (citasState as UiState.Success<List<com.example.veterinaria.data.model.CitaDTO>>).data
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    list.forEach { cita ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = cita.motivo ?: cita.observaciones ?: "Cita", fontWeight = FontWeight.Bold)
                                    Text(text = cita.fechaHora ?: "-", style = MaterialTheme.typography.bodySmall)
                                }
                                Button(onClick = { onNavigateToDetail(cita.id ?: 0L) }) { Text("Ver") }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = onNavigateToCreate, modifier = Modifier.fillMaxWidth()) { Text("Crear Cita") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaDetailScreen(
    citaViewModel: CitaViewModel,
    citaId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    val vm = citaViewModel
    val detalleState by vm.citaDetailState.collectAsState()

    LaunchedEffect(citaId) { vm.loadCitaById(citaId) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Detalle Cita") }, navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } })

        when (detalleState) {
            is UiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text((detalleState as UiState.Error).message) }
            is UiState.Success -> {
                val cita = (detalleState as UiState.Success<com.example.veterinaria.data.model.CitaDTO>).data
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(text = cita.motivo ?: cita.observaciones ?: "Cita", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    cita.fechaHora.let { Text(text = "Fecha: $it") }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onNavigateToEdit(citaId) }) { Text("Editar Cita") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaCreateEditScreen(
    citaViewModel: CitaViewModel,
    mascotaViewModel: com.example.veterinaria.viewmodel.MascotaViewModel,
    citaId: Long?,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isEditing = citaId != null
    val vm = citaViewModel
    val saveState by vm.saveCitaState.collectAsState()

    LaunchedEffect(Unit) { if (isEditing && citaId != null) vm.loadCitaById(citaId) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(if (isEditing) "Editar Cita" else "Crear Cita") }, navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } })

        // Load user's mascotas for selection
        val mascotasState by mascotaViewModel.mascotasState.collectAsState()
        var selectedMascotaId by remember { mutableStateOf(0L) }
        var motivo by remember { mutableStateOf("") }
        var fechaHora by remember { mutableStateOf("") }

        val context = LocalContext.current
        val authRepositoryLocal = remember { com.example.veterinaria.data.repository.AuthRepository(context) }
        val isLoggedIn by authRepositoryLocal.isLoggedIn.collectAsState(initial = false)

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            if (!isLoggedIn) {
                Text(text = "Debes iniciar sesión para crear una cita.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) { Text("Iniciar sesión") }
                return@Column
            }

            // Mascota selector
            when (mascotasState) {
                is UiState.Loading -> Text("Cargando mascotas...")
                is UiState.Error -> Text("Error cargando mascotas")
                is UiState.Success -> {
                    val list = (mascotasState as UiState.Success<List<com.example.veterinaria.data.model.MascotaDTO>>).data
                    if (list.isEmpty()) {
                        Text("No tienes mascotas. Crea una antes de agendar una cita.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { /* navegar a crear mascota */ }, modifier = Modifier.fillMaxWidth()) { Text("Crear Mascota") }
                        return@Column
                    }

                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            value = list.find { it.id == selectedMascotaId }?.nombre ?: "Seleccionar mascota",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Mascota") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )

                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            list.forEach { m ->
                                DropdownMenuItem(text = { Text(m.nombre) }, onClick = { selectedMascotaId = m.id ?: 0L; expanded = false })
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = motivo, onValueChange = { motivo = it }, label = { Text("Motivo") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            // Simple datetime input (text). Could be replaced with a proper picker.
            OutlinedTextField(value = fechaHora, onValueChange = { fechaHora = it }, label = { Text("Fecha y hora (ISO-8601)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))

            com.example.veterinaria.ui.components.VetButton(text = if (isEditing) "Actualizar" else "Crear", onClick = {
                val dto = com.example.veterinaria.data.model.CitaDTO(
                    id = citaId,
                    fechaHora = fechaHora,
                    estado = "PENDIENTE",
                    motivo = motivo,
                    clienteId = 0L,
                    mascotaId = selectedMascotaId
                )
                vm.createOrUpdateCita(citaId, dto)
            }, modifier = Modifier.fillMaxWidth(), enabled = selectedMascotaId != 0L && motivo.isNotBlank())

            when (saveState) {
                is UiState.Loading -> { }
                is UiState.Error -> Text(text = (saveState as UiState.Error).message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> LaunchedEffect(saveState) { onSaveSuccess() }
            }
        }
    }
}