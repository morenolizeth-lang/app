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
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.applicacion.R
import com.example.applicacion.viewmodel.EquipoViewModel

@Composable
fun EntrenadoresScreen(
    viewModel: EquipoViewModel,
    navController: NavController
) {

    val entrenadores = viewModel.entrenadores
    val equipos = viewModel.equipos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // 🔥 HEADER (EL TUYO EXACTO)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
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
                    painter = painterResource(id = R.drawable.entrenador),
                    contentDescription = "Equipo",
                    modifier = Modifier.size(45.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Administra los entrenadores",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📦 LISTA DE ENTRENADORES
        entrenadores.forEach { entrenador ->

            val equipoNombre =
                equipos.find { it.id == entrenador.equipoId }?.nombre ?: "Sin equipo"

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

                    // 🔥 NOMBRE (con degradado)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Red, Color.Black)
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = entrenador.nombre,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 📋 DATOS
                    Row(modifier = Modifier.fillMaxWidth()) {

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, Color.Black)
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Equipo", fontSize = 14.sp, color = Color.Gray)
                            Text(equipoNombre, fontWeight = FontWeight.Bold)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, Color.Black)
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Especialidad", fontSize = 14.sp, color = Color.Gray)
                            Text(entrenador.especialidad, fontWeight = FontWeight.Bold)
                        }
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