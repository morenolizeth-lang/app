package com.example.applicacion.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicacion.model.Equipo
import com.example.applicacion.model.Partido
import com.example.applicacion.repository.PartidoRepository
import com.example.applicacion.viewmodel.EquipoViewModel
import com.example.applicacion.viewmodel.PartidosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PartidosScreen(
    navController: NavController,
    viewModel: PartidosViewModel = viewModel()
) {
    val partidos = viewModel.partidos
    val cargando = viewModel.cargando
    val error = viewModel.error

    // 🔥 PARA EL DIÁLOGO
    var partidoSeleccionado by remember { mutableStateOf<Partido?>(null) }
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
                    viewModel.cargarPartidos()
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
                            text = "Cargando partidos...",
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
                    onClick = { viewModel.cargarPartidos() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reintentar", color = Color.White)
                }
                return@Column
            }
        }

        // 📭 LISTA VACÍA
        if (partidos.isEmpty() && !mostrarCarga && error == null) {
            Text(
                text = "No hay partidos registrados.",
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        // 📦 LISTA DE PARTIDOS (con clickable para diálogo)
        partidos.forEach { partido ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable {
                        partidoSeleccionado = partido
                        mostrarDialogo = true
                    }
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
                        Text(
                            text = partido.nombreEquipoLocal ?: "Sin equipo",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Text(
                            text = "${partido.golesLocal} VS ${partido.golesVisitante}",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Text(
                            text = partido.nombreEquipoVisitante ?: "Sin equipo",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
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

    // 🔥 DIALOGO PARA ACTUALIZAR/ELIMINAR PARTIDO (CORREGIDO)
    if (mostrarDialogo && partidoSeleccionado != null) {
        var fechadelPartido by remember { mutableStateOf(partidoSeleccionado!!.fechadelPartido) }
        var estadio by remember { mutableStateOf(partidoSeleccionado!!.estadio) }
        var golesLocal by remember { mutableStateOf(partidoSeleccionado!!.golesLocal.toString()) }
        var golesVisitante by remember { mutableStateOf(partidoSeleccionado!!.golesVisitante.toString()) }
        var equipoLocalSeleccionado by remember { mutableStateOf<Equipo?>(null) }
        var equipoVisitanteSeleccionado by remember { mutableStateOf<Equipo?>(null) }
        var expandidoLocal by remember { mutableStateOf(false) }
        var expandidoVisitante by remember { mutableStateOf(false) }

        val equipoViewModel: EquipoViewModel = viewModel()
        val equipos = equipoViewModel.equipos

        // Cargar equipos si es necesario
        LaunchedEffect(Unit) {
            if (equipos.isEmpty() && !equipoViewModel.cargando) {
                equipoViewModel.cargarEquipos()
            }
        }

        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                partidoSeleccionado = null
            },
            containerColor = Color.White,
            title = {
                Text("Actualizar Partido", fontWeight = FontWeight.Bold, color = Color.Black)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // ✅ CORREGIDO - Sin textColor, usando colors en su lugar
                    OutlinedTextField(
                        value = fechadelPartido,
                        onValueChange = { fechadelPartido = it },
                        label = { Text("Fecha (yyyy-MM-dd)", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = estadio,
                        onValueChange = { estadio = it },
                        label = { Text("Estadio", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = golesLocal,
                        onValueChange = { golesLocal = it },
                        label = { Text("Goles Local", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = golesVisitante,
                        onValueChange = { golesVisitante = it },
                        label = { Text("Goles Visitante", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // SELECTOR EQUIPO LOCAL
                    if (equipoViewModel.cargando) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Red
                            )
                        }
                    } else {
                        Text("Equipo Local", fontSize = 12.sp, color = Color.Black)
                        Button(
                            onClick = { expandidoLocal = !expandidoLocal },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                equipoLocalSeleccionado?.nombre
                                    ?: partidoSeleccionado!!.nombreEquipoLocal
                                    ?: "Seleccionar equipo local",
                                color = Color.Black
                            )
                        }

                        if (expandidoLocal) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 150.dp)
                                    .verticalScroll(rememberScrollState())
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                    .background(Color.White)
                            ) {
                                equipos.forEach { equipo ->
                                    TextButton(
                                        onClick = {
                                            equipoLocalSeleccionado = equipo
                                            expandidoLocal = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(equipo.nombre, color = Color.Black)
                                    }
                                    HorizontalDivider(color = Color.LightGray)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // SELECTOR EQUIPO VISITANTE
                    if (!equipoViewModel.cargando) {
                        Text("Equipo Visitante", fontSize = 12.sp, color = Color.Black)
                        Button(
                            onClick = { expandidoVisitante = !expandidoVisitante },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                equipoVisitanteSeleccionado?.nombre
                                    ?: partidoSeleccionado!!.nombreEquipoVisitante
                                    ?: "Seleccionar equipo visitante",
                                color = Color.Black
                            )
                        }

                        if (expandidoVisitante) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 150.dp)
                                    .verticalScroll(rememberScrollState())
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                    .background(Color.White)
                            ) {
                                equipos.forEach { equipo ->
                                    TextButton(
                                        onClick = {
                                            equipoVisitanteSeleccionado = equipo
                                            expandidoVisitante = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(equipo.nombre, color = Color.Black)
                                    }
                                    HorizontalDivider(color = Color.LightGray)
                                }
                            }
                        }
                    }
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
                        // Botón ELIMINAR
                        Button(
                            onClick = {
                                viewModel.eliminarPartido(partidoSeleccionado!!.id)
                                mostrarDialogo = false
                                partidoSeleccionado = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Eliminar", color = Color.White)
                        }

                        // Botón GUARDAR
                        Button(
                            onClick = {
                                val gl = golesLocal.toIntOrNull() ?: 0
                                val gv = golesVisitante.toIntOrNull() ?: 0
                                val idLocal = equipoLocalSeleccionado?.id ?: partidoSeleccionado!!.idEquipoLocal
                                val idVisitante = equipoVisitanteSeleccionado?.id ?: partidoSeleccionado!!.idEquipoVisitante

                                viewModel.actualizarPartido(
                                    partidoSeleccionado!!.id,
                                    fechadelPartido,
                                    estadio,
                                    gl,
                                    gv,
                                    idLocal,
                                    idVisitante
                                )
                                mostrarDialogo = false
                                partidoSeleccionado = null
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
                                partidoSeleccionado = null
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