package com.example.applicacion.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.applicacion.model.Jugador
import com.example.applicacion.viewmodel.JugadorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JugadoresScreen(
    viewModel: JugadorViewModel,
    navController: NavController,
    idEquipo: Long
) {
    val jugadores = viewModel.jugadores
    val cargando = viewModel.cargando
    val error = viewModel.error

    // 🔥 PARA EL DIÁLOGO
    var jugadorSeleccionado by remember { mutableStateOf<Jugador?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // 🔥 ESTADO LOCAL PARA CONTROLAR LA CARGA PERSISTENTE EN ERROR 500
    var reintentando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 🔥 AUTO-RETRY PARA ERROR 500 CON CARGA PERSISTENTE
    LaunchedEffect(error) {
        if (error != null && error.contains("500") && !reintentando) {
            reintentando = true
            while (true) {
                delay(3000)
                scope.launch {
                    viewModel.cargarJugadoresPorEquipo(idEquipo)
                }
                delay(2000)
                if (viewModel.error == null || !viewModel.error!!.contains("500")) {
                    reintentando = false
                    break
                }
            }
        }
    }

    // 🔥 MOSTRAR CARGA MIENTRAS: cargando=true O (error es 500 Y reintentando=true)
    val mostrarCarga = cargando || (error?.contains("500") == true && reintentando)

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

        // ⏳ CARGANDO CON AUTO-RETRY (PERSISTENTE)
        if (mostrarCarga) {
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

                    if (error?.contains("500") == true && reintentando) {
                        Text(
                            text = "Error de conexión (500). Reintentando automáticamente...",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    } else if (cargando && error == null) {
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

        // ❌ ERROR (SOLO PARA NO-500)
        error?.let {
            if (!it.contains("500")) {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { viewModel.cargarJugadoresPorEquipo(idEquipo) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reintentar", color = Color.White)
                }
                return@Column
            }
        }

        // 📦 LISTA DE JUGADORES (con clickable para diálogo)
        if (jugadores.isEmpty() && !mostrarCarga && error == null) {
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
                        .clickable {
                            jugadorSeleccionado = jugador
                            mostrarDialogo = true
                        }
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
                        Text("Nacionalidad: ${jugador.nacionalidad}", color = Color.Black)
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

    // 🔥 DIALOGO PARA ACTUALIZAR/ELIMINAR JUGADOR
    if (mostrarDialogo && jugadorSeleccionado != null) {
        var nombre by remember { mutableStateOf(jugadorSeleccionado!!.nombre) }
        var posicion by remember { mutableStateOf(jugadorSeleccionado!!.posicion) }
        var dorsal by remember { mutableStateOf(jugadorSeleccionado!!.dorsal.toString()) }
        var fechaNacimiento by remember { mutableStateOf(jugadorSeleccionado!!.fechaNacimiento) }
        var nacionalidad by remember { mutableStateOf(jugadorSeleccionado!!.nacionalidad) }

        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                jugadorSeleccionado = null
            },
            containerColor = Color.White,
            title = {
                Text("Actualizar Jugador", fontWeight = FontWeight.Bold, color = Color.Black)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // NOMBRE - Label gris, texto negro
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // POSICIÓN - Label gris, texto negro
                    OutlinedTextField(
                        value = posicion,
                        onValueChange = { posicion = it },
                        label = { Text("Posición", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // DORSAL - Label gris, texto negro
                    OutlinedTextField(
                        value = dorsal,
                        onValueChange = { dorsal = it },
                        label = { Text("Dorsal", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // FECHA NACIMIENTO - Label gris, texto negro
                    OutlinedTextField(
                        value = fechaNacimiento,
                        onValueChange = { fechaNacimiento = it },
                        label = { Text("Fecha Nacimiento (yyyy-MM-dd)", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // NACIONALIDAD - Label gris, texto negro
                    OutlinedTextField(
                        value = nacionalidad,
                        onValueChange = { nacionalidad = it },
                        label = { Text("Nacionalidad", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        // Botón ELIMINAR (negro)
                        Button(
                            onClick = {
                                viewModel.eliminarJugador(jugadorSeleccionado!!.id)
                                mostrarDialogo = false
                                jugadorSeleccionado = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Eliminar", color = Color.White)
                        }

                        // Botón GUARDAR (rojo)
                        Button(
                            onClick = {
                                viewModel.actualizarJugador(
                                    jugadorSeleccionado!!.id,
                                    nombre,
                                    posicion,
                                    dorsal.toIntOrNull() ?: 0,
                                    fechaNacimiento,
                                    nacionalidad,
                                    idEquipo
                                )
                                mostrarDialogo = false
                                jugadorSeleccionado = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Guardar", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón CANCELAR centrado
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = {
                                mostrarDialogo = false
                                jugadorSeleccionado = null
                            }
                        ) {
                            Text("Cancelar", color = Color.Gray)
                        }
                    }
                }
            },
            dismissButton = {}
        )
    }
}