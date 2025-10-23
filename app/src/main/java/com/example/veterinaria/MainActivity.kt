package com.example.veterinaria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.data.repository.MascotaRepository
import com.example.veterinaria.data.repository.ClienteRepository
import com.example.veterinaria.data.repository.CitaRepository
import com.example.veterinaria.viewmodel.ClienteViewModel
import com.example.veterinaria.viewmodel.CitaViewModel
import com.example.veterinaria.navigation.VeterinariaNavigation
import com.example.veterinaria.viewmodel.MascotaViewModel
import com.example.veterinaria.ui.theme.VeterinariaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeterinariaTheme {
                VeterinariaApp()
            }
        }
    }
}

@Composable
fun VeterinariaApp() {
    val navController = rememberNavController()
    val context = navController.context
    val authRepository = remember { AuthRepository(context) }
    val mascotaRepository = remember { MascotaRepository(authRepository) }
    val clienteRepository = remember { ClienteRepository(authRepository) }
    val citaRepository = remember { CitaRepository(authRepository) }

    // Create a shared MascotaViewModel for use across navigation destinations
    val mascotaViewModel: MascotaViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MascotaViewModel(mascotaRepository, authRepository) as T
        }

        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
            return create(modelClass)
        }
    })

    val clienteViewModel: ClienteViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ClienteViewModel(clienteRepository, authRepository) as T
        }

        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
            return create(modelClass)
        }
    })

    val citaViewModel: CitaViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CitaViewModel(citaRepository, authRepository) as T
        }

        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
            return create(modelClass)
        }
    })
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        VeterinariaNavigation(
            navController = navController,
            authRepository = authRepository,
            mascotaViewModel = mascotaViewModel,
            clienteViewModel = clienteViewModel,
            citaViewModel = citaViewModel
        )
    }
}