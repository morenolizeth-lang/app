package com.example.applicacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.applicacion.view.*
import com.example.applicacion.viewmodel.EquipoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel: EquipoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "inicio"  // ✅ CORRECCIÓN: empieza en inicio
    ) {

        composable("inicio") {       // ✅ ruta registrada
            InicioScreen(navController)
        }

        composable("menu") {
            MenuScreen(navController)
        }

        composable("equipos") {
            EquiposScreen(viewModel, navController)
        }

        composable("jugadores") {
            JugadoresScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("todosJugadores") {
            TodosJugadoresScreen(navController = navController)
        }

        composable("entrenadores") {
            EntrenadoresScreen(viewModel, navController)
        }

        composable("partidos") {
            PartidosScreen(navController)
        }
    }
}