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
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.navigation.VeterinariaNavigation
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
    val authRepository = AuthRepository(navController.context)
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        VeterinariaNavigation(
            navController = navController,
            authRepository = authRepository
        )
    }
}