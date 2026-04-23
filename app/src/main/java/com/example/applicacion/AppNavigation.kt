package com.example.applicacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.applicacion.view.*
import com.example.applicacion.viewmodel.EquipoViewModel
import com.example.applicacion.viewmodel.EntrenadorViewModel
import com.example.applicacion.viewmodel.JugadorViewModel  // ✅ nuevo import
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val equipoViewModel: EquipoViewModel = viewModel()
    val entrenadorViewModel: EntrenadorViewModel = viewModel()
    val jugadorViewModel: JugadorViewModel = viewModel()  // ✅ nuevo viewModel

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {

        composable("inicio") {
            InicioScreen(navController)
        }

        composable("menu") {
            MenuScreen(navController)
        }

        composable("equipos") {
            EquiposScreen(
                viewModel = equipoViewModel,
                jugadorViewModel = jugadorViewModel,  // ✅ nuevo parámetro
                navController = navController
            )
        }

        composable("jugadores") {
            JugadoresScreen(
                viewModel = jugadorViewModel,  // ✅ cambio de equipoViewModel
                navController = navController
            )
        }

        composable("todosJugadores") {
            TodosJugadoresScreen(navController = navController)
        }

        composable("entrenadores") {
            EntrenadoresScreen(entrenadorViewModel, navController)
        }

        composable("partidos") {
            PartidosScreen(navController)
        }
    }
}