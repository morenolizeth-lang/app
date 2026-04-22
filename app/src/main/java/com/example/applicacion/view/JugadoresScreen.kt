package com.example.applicacion.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicacion.R
import com.example.applicacion.viewmodel.EquipoViewModel

@Composable
fun JugadoresScreen(
    viewModel: EquipoViewModel,
    navController: NavController
) {
    val jugadores = viewModel.jugadores

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // 🔥 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 3.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Red, Color.Black)
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
                .background(Color.White)
                .padding(vertical = 18.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jugador),
                    contentDescription = "jugador",
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Jugadores",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📦 LISTA DE JUGADORES
        if (jugadores.isEmpty()) {
            Text(
                text = "Este equipo no tiene jugadores registrados.",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            jugadores.forEach { jugador ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Color.Red, Color.Black)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(jugador.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Posición: ${jugador.posicion}", color = Color.Black)
                        Text("Dorsal: ${jugador.dorsal}", color = Color.Black)
                        Text("Nacimiento: ${jugador.fechaNacimiento}", color = Color.Black)
                        Text("Nacionalidad: ${jugador.Nacionalidad}", color = Color.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔙 BOTÓN REGRESAR
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar")
        }
    }
}