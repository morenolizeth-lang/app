package com.example.applicacion.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.applicacion.R
import com.example.applicacion.viewmodel.EquipoViewModel
import com.example.applicacion.viewmodel.JugadorViewModel  // ✅ nuevo import
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.navigation.NavController

@Composable
fun EquiposScreen(
    viewModel: EquipoViewModel,
    jugadorViewModel: JugadorViewModel,
    navController: NavController
) {

    val equipos = viewModel.equipos
    val golesEquipo = viewModel.golesEquipo
    val cargando = viewModel.cargando   // ✅
    val error = viewModel.error         // ✅

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // 🔥 HEADER (igual)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(listOf(Color.Red, Color.Black)),
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Color.White)
                .padding(vertical = 18.dp, horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.equipo),
                    contentDescription = "equipo",
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Equipos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ⏳ CARGANDO
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Red
            )
        }

        // ❌ ERROR + REINTENTAR
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = { viewModel.cargarEquipos() }, // ✅ REINTENTO
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Reintentar")
            }
        }

        // 📭 LISTA VACÍA
        if (equipos.isEmpty() && !cargando && error == null) {
            Text(
                text = "No hay equipos registrados.",
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        // 📦 LISTA DE EQUIPOS (tu código igual)
        equipos.forEach { equipo ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Red, Color.Black)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.5.dp, Color.Black)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nombre", fontSize = 14.sp, color = Color.Gray)
                            Text(
                                equipo.nombre,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    brush = Brush.linearGradient(
                                        listOf(Color.Red, Color.Black)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Goles", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = (golesEquipo[equipo.id] ?: 0).toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, Color.Black)
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Fecha", fontSize = 14.sp, color = Color.Gray)
                            Text(equipo.fecha, fontWeight = FontWeight.Bold)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, Color.Black)
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Ciudad", fontSize = 14.sp, color = Color.Gray)
                            Text(equipo.ciudad, fontWeight = FontWeight.Bold)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color.Red, Color.Black)
                                )
                            )
                    ) {
                        Button(
                            onClick = {
                                jugadorViewModel.cargarJugadores(equipo)
                                navController.navigate("jugadores/${equipo.id}")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Jugadores",
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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