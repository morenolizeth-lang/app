package com.example.applicacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.applicacion.view.*
import com.example.applicacion.viewmodel.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val equipoViewModel: EquipoViewModel = viewModel()
    val entrenadorViewModel: EntrenadorViewModel = viewModel()
    val jugadorViewModel: JugadorViewModel = viewModel()

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
                jugadorViewModel = jugadorViewModel,
                navController = navController
            )
        }

        // ✅ AQUÍ ESTÁ LA CORRECCIÓN IMPORTANTE
        composable(
            route = "jugadores/{idEquipo}",
            arguments = listOf(navArgument("idEquipo") {
                type = NavType.LongType
            })
        ) { backStackEntry ->

            val idEquipo = backStackEntry.arguments?.getLong("idEquipo") ?: 0L

            JugadoresScreen(
                viewModel = jugadorViewModel,
                navController = navController,
                idEquipo = idEquipo
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