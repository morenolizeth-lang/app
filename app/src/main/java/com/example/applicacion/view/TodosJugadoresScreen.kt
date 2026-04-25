package com.example.applicacion.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.applicacion.model.Jugador
import com.example.applicacion.viewmodel.JugadorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    val listaAMostrar = if (busquedaActiva) jugadoresFiltrados else jugadores

    var jugadorSeleccionado by remember { mutableStateOf<Jugador?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // 🔥 AUTO-RETRY PARA ERROR 500
    LaunchedEffect(error) {
        if (error != null && error.contains("500")) {
            delay(3000)
            viewModel.cargarJugadores()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // HEADER
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
            Row(verticalAlignment = Alignment.CenterVertically) {
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

        // 🔥 ESTADO DE CARGA CON AUTO-RETRY
        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (error?.contains("500") == true) {
                        Text(
                            text = "Error de conexión (500). Reintentando automáticamente...",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    } else {
                        Text(
                            text = "Cargando jugadores...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            return@Column
        }

        // 🔥 OTROS ERRORES (no 500)
        error?.let {
            if (!it.contains("500")) {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { viewModel.cargarJugadores() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                ) {
                    Text("Reintentar")
                }
                return@Column
            }
        }

        // BUSCADOR
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
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number,  // ← Esto es KeyboardType, no KeywordType
                    imeAction = ImeAction.Search
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onSearch = {
                        viewModel.buscarJugadoresPorGoles()
                        keyboardController?.hide()
                    }
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
        Spacer(modifier = Modifier.height(16.dp))

        listaAMostrar.forEach { jugador ->
            Box(
                modifier = Modifier.clickable {
                    jugadorSeleccionado = jugador
                    mostrarDialogo = true
                }
            ) {
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

    // DIALOGO
    if (mostrarDialogo && jugadorSeleccionado != null) {
        var nombre by remember { mutableStateOf(jugadorSeleccionado!!.nombre) }
        var posicion by remember { mutableStateOf(jugadorSeleccionado!!.posicion) }
        var dorsal by remember { mutableStateOf(jugadorSeleccionado!!.dorsal.toString()) }

        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                jugadorSeleccionado = null
            },
            containerColor = Color.White,
            title = {
                Text("Jugador", fontWeight = FontWeight.Bold, color = Color.Black)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,      // Negro cuando escribes
                            unfocusedTextColor = Color.Black,    // Negro cuando no escribes
                            focusedLabelColor = Color.Gray,      // Label gris cuando enfocado
                            unfocusedLabelColor = Color.Gray,    // Label gris cuando no enfocado
                            focusedBorderColor = Color.Red,      // Borde rojo cuando enfocado
                            unfocusedBorderColor = Color.Gray    // Borde gris cuando no enfocado
                        )
                    )

                    OutlinedTextField(
                        value = posicion,
                        onValueChange = { posicion = it },
                        label = { Text("Posición") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    OutlinedTextField(
                        value = dorsal,
                        onValueChange = { dorsal = it },
                        label = { Text("Dorsal") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }
            },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.eliminarJugador(jugadorSeleccionado!!.id)
                            viewModel.cargarJugadores()
                            mostrarDialogo = false
                            jugadorSeleccionado = null
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Eliminar", color = Color.White)
                    }

                    Button(
                        onClick = {
                            viewModel.actualizarJugador(
                                jugadorSeleccionado!!.id,
                                nombre,
                                posicion,
                                dorsal.toIntOrNull() ?: 0,
                                jugadorSeleccionado!!.fechaNacimiento,
                                jugadorSeleccionado!!.nacionalidad,
                                jugadorSeleccionado!!.idEquipo
                            )
                            viewModel.cargarJugadores()
                            mostrarDialogo = false
                            jugadorSeleccionado = null
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Guardar", color = Color.White)
                    }
                }
            },
            dismissButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = {
                        mostrarDialogo = false
                        jugadorSeleccionado = null
                    }) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }
}

// 🔥 UNA SOLA DEFINICIÓN DE ESTA FUNCIÓN
@Composable
fun JugadorCardConEstadisticas(
    jugador: Jugador,
    viewModel: JugadorViewModel,
    estadisticasAbiertas: Map<Long, List<com.example.applicacion.model.EstadisticaJugador>>
) {
    val estadisticasVisibles = estadisticasAbiertas.containsKey(jugador.id)
    val estadisticas = estadisticasAbiertas[jugador.id] ?: emptyList()
    val errorEstadisticas = viewModel.errorEstadisticas

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
                        text = viewModel.getNombreEquipo(jugador),
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
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(if (estadisticasVisibles) "Ocultar estadísticas" else "Ver estadísticas")
            }

            errorEstadisticas?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }

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

// 🔥 UNA SOLA DEFINICIÓN DE ESTA FUNCIÓN
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