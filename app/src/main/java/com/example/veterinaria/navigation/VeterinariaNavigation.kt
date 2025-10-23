package com.example.veterinaria.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.veterinaria.data.repository.AuthRepository
import com.example.veterinaria.screen.auth.LoginScreen
import com.example.veterinaria.screen.auth.RegisterScreen
import com.example.veterinaria.screen.dashboard.DashboardScreen
import com.example.veterinaria.screen.cliente.ClientesScreen
import com.example.veterinaria.screen.cliente.ClienteDetailScreen
import com.example.veterinaria.screen.cliente.ClienteCreateEditScreen
import com.example.veterinaria.screen.mascota.MascotasScreen
import com.example.veterinaria.screen.mascota.MascotaDetailScreen
import com.example.veterinaria.screen.mascota.MascotaCreateEditScreen
import com.example.veterinaria.viewmodel.MascotaViewModel
import com.example.veterinaria.viewmodel.ClienteViewModel
import com.example.veterinaria.viewmodel.CitaViewModel
import com.example.veterinaria.screen.cita.CitasScreen
import com.example.veterinaria.screen.cita.CitaDetailScreen
import com.example.veterinaria.screen.cita.CitaCreateEditScreen
import com.example.veterinaria.util.Constants.Navigation

@Composable
fun VeterinariaNavigation(
    navController: NavHostController,
    authRepository: AuthRepository,
    mascotaViewModel: MascotaViewModel,
    clienteViewModel: ClienteViewModel,
    citaViewModel: CitaViewModel
) {
    val isLoggedIn by authRepository.isLoggedIn.collectAsState(initial = false)
    
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Navigation.DASHBOARD_ROUTE else Navigation.LOGIN_ROUTE
    ) {
        // Pantallas de autenticación
        composable(Navigation.LOGIN_ROUTE) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Navigation.REGISTER_ROUTE)
                },
                onLoginSuccess = {
                    navController.navigate(Navigation.DASHBOARD_ROUTE) {
                        popUpTo(Navigation.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Navigation.REGISTER_ROUTE) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Navigation.LOGIN_ROUTE) {
                        popUpTo(Navigation.REGISTER_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        
        // Dashboard principal
        composable(Navigation.DASHBOARD_ROUTE) {
            DashboardScreen(
                onNavigateToClientes = {
                    navController.navigate(Navigation.CLIENTES_ROUTE)
                },
                onNavigateToMascotas = {
                    navController.navigate(Navigation.MASCOTAS_ROUTE)
                },
                onNavigateToCitas = {
                    navController.navigate(Navigation.CITAS_ROUTE)
                },
                onLogout = {
                    navController.navigate(Navigation.LOGIN_ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        // Clientes
        composable(Navigation.CLIENTES_ROUTE) {
            ClientesScreen(
                clienteViewModel = clienteViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { clienteId ->
                    navController.navigate("clientes/$clienteId")
                },
                onNavigateToCreate = {
                    navController.navigate(Navigation.CLIENTES_CREATE_ROUTE)
                },
                onNavigateToEdit = { clienteId ->
                    navController.navigate("clientes/$clienteId/edit")
                }
            )
        }
        
        composable(
            route = Navigation.CLIENTES_DETAIL_ROUTE,
            arguments = listOf(navArgument("clienteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getLong("clienteId") ?: 0L
            ClienteDetailScreen(
                clienteViewModel = clienteViewModel,
                clienteId = clienteId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate("clientes/$id/edit")
                },
                onNavigateToMascotas = { id ->
                    navController.navigate("mascotas?clienteId=$id")
                }
            )
        }
        
        composable(Navigation.CLIENTES_CREATE_ROUTE) {
            ClienteCreateEditScreen(
                clienteViewModel = clienteViewModel,
                clienteId = null,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Navigation.CLIENTES_EDIT_ROUTE,
            arguments = listOf(navArgument("clienteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getLong("clienteId") ?: 0L
            ClienteCreateEditScreen(
                clienteViewModel = clienteViewModel,
                clienteId = clienteId,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
        
        // Mascotas
        composable(Navigation.MASCOTAS_ROUTE) {
            MascotasScreen(
                mascotaViewModel = mascotaViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { mascotaId ->
                    navController.navigate("mascotas/$mascotaId")
                },
                onNavigateToCreate = {
                    navController.navigate(Navigation.MASCOTAS_CREATE_ROUTE)
                },
                onNavigateToEdit = { mascotaId ->
                    navController.navigate("mascotas/$mascotaId/edit")
                }
            )
        }
        
        composable(
            route = Navigation.MASCOTAS_DETAIL_ROUTE,
            arguments = listOf(navArgument("mascotaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getLong("mascotaId") ?: 0L
            MascotaDetailScreen(
                mascotaViewModel = mascotaViewModel,
                mascotaId = mascotaId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate("mascotas/$id/edit")
                },
                onNavigateToCitas = { id ->
                    navController.navigate("citas?mascotaId=$id")
                }
            )
        }
        
        composable(Navigation.MASCOTAS_CREATE_ROUTE) {
            MascotaCreateEditScreen(
                mascotaViewModel = mascotaViewModel,
                mascotaId = null,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(Navigation.LOGIN_ROUTE) }
            )
        }
        
        composable(
            route = Navigation.MASCOTAS_EDIT_ROUTE,
            arguments = listOf(navArgument("mascotaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getLong("mascotaId") ?: 0L
            MascotaCreateEditScreen(
                mascotaViewModel = mascotaViewModel,
                mascotaId = mascotaId,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(Navigation.LOGIN_ROUTE) }
            )
        }
        
        // Citas
        composable(Navigation.CITAS_ROUTE) {
            CitasScreen(
                citaViewModel = citaViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { citaId ->
                    navController.navigate("citas/$citaId")
                },
                onNavigateToCreate = {
                    navController.navigate(Navigation.CITAS_CREATE_ROUTE)
                },
                onNavigateToEdit = { citaId ->
                    navController.navigate("citas/$citaId/edit")
                }
            )
        }
        
        composable(
            route = Navigation.CITAS_DETAIL_ROUTE,
            arguments = listOf(navArgument("citaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getLong("citaId") ?: 0L
            CitaDetailScreen(
                citaViewModel = citaViewModel,
                citaId = citaId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate("citas/$id/edit")
                }
            )
        }
        
        composable(Navigation.CITAS_CREATE_ROUTE) {
            CitaCreateEditScreen(
                citaViewModel = citaViewModel,
                mascotaViewModel = mascotaViewModel,
                citaId = null,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(Navigation.LOGIN_ROUTE) }
            )
        }
        
        composable(
            route = Navigation.CITAS_EDIT_ROUTE,
            arguments = listOf(navArgument("citaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getLong("citaId") ?: 0L
            CitaCreateEditScreen(
                citaViewModel = citaViewModel,
                mascotaViewModel = mascotaViewModel,
                citaId = citaId,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(Navigation.LOGIN_ROUTE) }
            )
        }
    }
}