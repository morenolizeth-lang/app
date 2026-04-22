package com.example.applicacion.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicacion.ui.theme.ApplicacionTheme
import com.example.applicacion.R
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// =======================
// SCREEN PRINCIPAL
// =======================
@Composable
fun MenuScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔥 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Red)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Área administrativa",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Accede a las opciones disponibles para consultar o modificar la información",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 🔥 BOTONES
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            BotonMenu(
                texto = "Equipos",
                icono = R.drawable.equipo,
                onClick = { navController.navigate("equipos") }
            )

            BotonMenu(
                texto = "Jugadores",
                icono = R.drawable.jugador,
                onClick = { navController.navigate("todosJugadores") }
            )

            BotonMenu(
                texto = "Entrenadores",
                icono = R.drawable.entrenador,
                onClick = { navController.navigate("entrenadores") }
            )

            BotonMenu(
                texto = "Partidos",
                icono = R.drawable.partido,
                onClick = { navController.navigate("partidos") }
            )
        }
    }
}

// =======================
// BOTÓN REUTILIZABLE
// =======================
@Composable
fun BotonMenu(
    texto: String,
    icono: Int,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Color.Red),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 12.dp,
            pressedElevation = 4.dp
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = icono),
                contentDescription = texto,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = texto,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =======================
// PREVIEW (SIN CRASH)
// =======================
@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    ApplicacionTheme {
        MenuScreen(
            navController = rememberNavController()
        )
    }
}