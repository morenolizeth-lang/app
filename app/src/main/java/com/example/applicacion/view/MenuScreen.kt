package com.example.applicacion.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

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

        HeaderPortada()

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

@Composable
fun HeaderPortada() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.Red, Color.Black)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {

        // 🖼️ IMAGEN
        Image(
            painter = painterResource(R.drawable.deporte),
            contentDescription = "deporte",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
        )

        // 🔺 TRIÁNGULO DERECHO
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(
                    GenericShape { size, _ ->
                        moveTo(size.width * 0.4f, 0f)
                        lineTo(size.width, 0f)
                        lineTo(size.width, size.height)
                        lineTo(size.width * 0.6f, size.height)
                        close()
                    }
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Red, Color.Black)
                    )
                )
        )

        // 📝 TEXTO
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .width(160.dp),
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = "\"Vive las eliminatorias minuto a minuto.\"",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.End
            )

        }
    }
}