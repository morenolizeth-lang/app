package com.example.applicacion.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.applicacion.R
import com.example.applicacion.viewmodel.JugadorViewModel

@Composable
fun TodosJugadoresScreen(
    navController: NavController,
    viewModel: JugadorViewModel = viewModel()
) {
    val jugadores = viewModel.jugadores
    val jugadoresFiltrados = viewModel.jugadoresFiltrados
    val busquedaActiva = viewModel.busquedaActiva
    val estadisticasAbiertas = viewModel.estadisticasAbiertas
    val golesBusqueda = viewModel.golesBusqueda
    val cargando = viewModel.cargando
    val error = viewModel.error
    val keyboardController = LocalSoftwareKeyboardController.current

    val listaAMostrar = if (busquedaActiva) jugadoresFiltrados else jugadores

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
                    brush = Brush.horizontalGradient(colors = listOf(Color.Red, Color.Black)),
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
                    text = "Todos los jugadores",
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

        // ❌ ERROR
        error?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            Button(                                    // ✅ agregar esto
                onClick = { viewModel.cargarJugadores() }, // ✅ recargar datos
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Reintentar")
            }
        }

        // 🔍 BARRA DE BÚSQUEDA POR GOLES
        Text(
            text = "Buscar por goles anotados",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = golesBusqueda,
                onValueChange = { viewModel.onGolesChange(it) },
                label = { Text("Más de X goles") },
                placeholder = { Text("ej: 0") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.buscarJugadoresPorGoles()
                        keyboardController?.hide()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Red,
                    unfocusedBorderColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.buscarJugadoresPorGoles()
                    keyboardController?.hide()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Buscar")
            }
        }

        AnimatedVisibility(visible = busquedaActiva) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { viewModel.limpiarBusqueda() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("✕ Limpiar búsqueda")
                }

                if (jugadoresFiltrados.isEmpty()) {
                    Text(
                        text = "Sin resultados",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp)
                    )
                } else {
                    Text(
                        text = "${jugadoresFiltrados.size} resultado(s)",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📦 LISTA DE JUGADORES
        if (listaAMostrar.isEmpty() && !busquedaActiva && !cargando) {
            Text(
                text = "No hay jugadores registrados.",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            listaAMostrar.forEach { jugador ->
                JugadorCardConEstadisticas(
                    jugador = jugador,
                    viewModel = viewModel,
                    estadisticasAbiertas = estadisticasAbiertas
                )
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

// 📌 CARD DE JUGADOR CON ESTADÍSTICAS
@Composable
fun JugadorCardConEstadisticas(
    jugador: com.example.applicacion.model.Jugador,
    viewModel: JugadorViewModel,
    estadisticasAbiertas: Map<Long, List<com.example.applicacion.model.EstadisticaJugador>>
) {
    val estadisticasVisibles = estadisticasAbiertas.containsKey(jugador.id)
    val estadisticas = estadisticasAbiertas[jugador.id] ?: emptyList()
    val errorEstadisticas = viewModel.errorEstadisticas  // ✅

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Black)),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = jugador.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Dorsal", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = jugador.dorsal.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .border(0.5.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Equipo", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = viewModel.getNombreEquipo(jugador.id),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.toggleEstadisticas(jugador.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black)
            ) {
                Text(if (estadisticasVisibles) "Ocultar estadísticas" else "Ver estadísticas")
            }

            // ❌ ERROR DE ESTADÍSTICAS ✅
            errorEstadisticas?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }

            // 📊 ESTADÍSTICAS DESPLEGABLES
            AnimatedVisibility(visible = estadisticasVisibles) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    if (estadisticas.isEmpty()) {
                        Text(
                            text = "Sin estadísticas registradas.",
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        estadisticas.forEach { stat ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .border(
                                        1.dp,
                                        Brush.horizontalGradient(listOf(Color.Red, Color.Black)),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = viewModel.getNombrePartido(stat.idPartido),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    StatChip(label = "Minutos", valor = stat.minutosJugados.toString())
                                    StatChip(label = "Goles", valor = stat.goles.toString())
                                    StatChip(label = "Asistencias", valor = stat.asistencias.toString())
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    StatChip(
                                        label = "Amarillas",
                                        valor = stat.tarjetasAmarillas.toString(),
                                        color = Color(0xFFFFD600)
                                    )
                                    StatChip(
                                        label = "Rojas",
                                        valor = stat.tarjetasRojas.toString(),
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 📌 CHIP DE ESTADÍSTICA
@Composable
fun StatChip(label: String, valor: String, color: Color = Color.Black) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(0.5.dp, color, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = valor, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Text(text = label, fontSize = 11.sp, color = Color.Gray)
    }
}