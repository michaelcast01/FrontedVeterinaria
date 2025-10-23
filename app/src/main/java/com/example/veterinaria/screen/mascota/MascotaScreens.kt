package com.example.veterinaria.screen.mascota

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.veterinaria.viewmodel.MascotaViewModel
import com.example.veterinaria.util.UiState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen(
    mascotaViewModel: com.example.veterinaria.viewmodel.MascotaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    val vm = mascotaViewModel
    val mascotasState by vm.mascotasState.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Mascotas") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        when (mascotasState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = (mascotasState as UiState.Error).message)
                }
            }
            is UiState.Success -> {
                val list = (mascotasState as UiState.Success<List<com.example.veterinaria.data.model.MascotaDTO>>).data
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    list.forEach { mascota ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = mascota.nombre ?: "-", fontWeight = FontWeight.Bold)
                                    Text(text = mascota.especie ?: "-", style = MaterialTheme.typography.bodySmall)
                                }
                                Button(onClick = { onNavigateToDetail(mascota.id ?: 0L) }) {
                                    Text("Ver")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(onClick = onNavigateToCreate, modifier = Modifier.fillMaxWidth()) {
                        Text("Crear Mascota")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaDetailScreen(
    mascotaViewModel: MascotaViewModel,
    mascotaId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToCitas: (Long) -> Unit
) {
    val vm = mascotaViewModel
    val detalleState by vm.mascotaDetailState.collectAsState()

    LaunchedEffect(mascotaId) { vm.loadMascotaById(mascotaId) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detalle Mascota") },
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
                val mascota = (detalleState as UiState.Success<com.example.veterinaria.data.model.MascotaDTO>).data
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(text = mascota.nombre ?: "-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Especie: ${mascota.especie}")
                    mascota.raza?.let { Text(text = "Raza: $it") }
                    mascota.sexo?.let { Text(text = "Sexo: $it") }
                    mascota.peso?.let { Text(text = "Peso: ${it} kg") }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { onNavigateToEdit(mascotaId) }) { Text("Editar Mascota") }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onNavigateToCitas(mascotaId) }) { Text("Ver Citas") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaCreateEditScreen(
    mascotaViewModel: MascotaViewModel,
    mascotaId: Long?,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isEditing = mascotaId != null
    val vmLocal = mascotaViewModel
    val formState by vmLocal.mascotaForm.collectAsState()
    val formValidation by vmLocal.formValidation.collectAsState()
    val saveState by vmLocal.saveMascotaState.collectAsState()
    val especiesState by vmLocal.especiesState.collectAsState()

    LaunchedEffect(Unit) {
        if (isEditing && mascotaId != null) vmLocal.loadMascotaById(mascotaId)
        vmLocal.loadEspecies()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(if (isEditing) "Editar Mascota" else "Crear Mascota") },
            navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(
                value = formState.nombre,
                onValueChange = { vmLocal.updateMascotaForm(nombre = it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Especie (dropdown básico)
            var expandedEspecie by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expandedEspecie, onExpandedChange = { expandedEspecie = !expandedEspecie }) {
                OutlinedTextField(
                    value = formState.especie,
                    onValueChange = { vmLocal.updateMascotaForm(especie = it) },
                    readOnly = true,
                    label = { Text("Especie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecie) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(expanded = expandedEspecie, onDismissRequest = { expandedEspecie = false }) {
                    when (especiesState) {
                        is UiState.Success -> {
                            (especiesState as UiState.Success<List<String>>).data.forEach { esp ->
                                DropdownMenuItem(text = { Text(esp) }, onClick = { vmLocal.updateMascotaForm(especie = esp); expandedEspecie = false })
                            }
                        }
                        else -> DropdownMenuItem(text = { Text("Cargando...") }, onClick = { expandedEspecie = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = formState.raza,
                onValueChange = { vmLocal.updateMascotaForm(raza = it) },
                label = { Text("Raza") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            val context = LocalContext.current
            val authRepositoryLocal = remember { com.example.veterinaria.data.repository.AuthRepository(context) }
            val isLoggedIn by authRepositoryLocal.isLoggedIn.collectAsState(initial = false)

            if (!isLoggedIn) {
                Text(text = "Debes iniciar sesión para crear una mascota.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) { Text("Iniciar sesión") }
            } else {
                com.example.veterinaria.ui.components.VetButton(
                    text = if (isEditing) "Actualizar" else "Crear",
                    onClick = { vmLocal.saveMascota(mascotaId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = formValidation.isValid
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (saveState) {
                is UiState.Loading -> { /* no mostrar */ }
                is UiState.Error -> Text(text = (saveState as UiState.Error).message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> LaunchedEffect(saveState) { onSaveSuccess() }
            }
        }
    }
}