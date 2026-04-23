package com.example.applicacion.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicacion.viewmodel.PartidosViewModel

@Composable
fun PartidosScreen(
    navController: NavController,
    viewModel: PartidosViewModel = viewModel()
) {
    val partidos = viewModel.partidos
    val cargando = viewModel.cargando
    val error = viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 3.dp,
                    brush = Brush.horizontalGradient(listOf(Color.Red, Color.Black)),
                    shape = RoundedCornerShape(0.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Partidos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ⏳ CARGANDO
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Red
            )
        }

        // ❌ ERROR
        error?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            Button(
                onClick = { viewModel.cargarPartidos() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Reintentar", color = Color.White)
            }
        }

        // 📭 LISTA VACÍA
        if (partidos.isEmpty() && !cargando && error == null) {
            Text(
                text = "No hay partidos registrados.",
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        // 📦 LISTA DE PARTIDOS
        partidos.forEach { partido ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .border(
                        2.dp,
                        Brush.linearGradient(listOf(Color.Red, Color.Black)),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "${partido.estadio} - ${partido.fechadelPartido}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // ✅ nombre directo desde el response
                        Text(
                            text = partido.nombreEquipoLocal,
                            fontWeight = FontWeight.Bold
                        )

                        // ✅ marcador
                        Text(
                            text = "${partido.golesLocal} VS ${partido.golesVisitante}",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        // ✅ nombre directo desde el response
                        Text(
                            text = partido.nombreEquipoVisitante,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Regresar")
        }
    }
}