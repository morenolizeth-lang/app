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
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.applicacion.R
import com.example.applicacion.model.Entrenador
import com.example.applicacion.model.Equipo
import com.example.applicacion.viewmodel.EntrenadorViewModel
import com.example.applicacion.viewmodel.EquipoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EntrenadoresScreen(
    viewModel: EntrenadorViewModel,
    navController: NavController
) {

    val entrenadores = viewModel.entrenadores
    val cargando = viewModel.cargando
    val error = viewModel.error

    // 🔥 PARA EL DIÁLOGO
    var entrenadorSeleccionado by remember { mutableStateOf<Entrenador?>(null) }
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
                    viewModel.cargarEntrenadores()
                }
                // Esperar a que termine la carga o haya otro error
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
                            text = "Cargando entrenadores...",
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
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    onClick = {
                        viewModel.cargarEntrenadores()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Reintentar")
                }
                return@Column
            }
        }

        // 📭 LISTA VACÍA
        if (entrenadores.isEmpty() && !mostrarCarga && error == null) {
            Text(
                text = "No hay entrenadores registrados.",
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        // 📦 LISTA DE ENTRENADORES (con clickable para diálogo)
        entrenadores.forEach { entrenador ->

            val equipoNombre = entrenador.nombreEquipo

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable {
                        entrenadorSeleccionado = entrenador
                        mostrarDialogo = true
                    }
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
                            Text(equipoNombre ?: "Sin equipo", fontWeight = FontWeight.Bold)
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

    // 🔥 DIALOGO PARA ACTUALIZAR/ELIMINAR ENTRENADOR
    if (mostrarDialogo && entrenadorSeleccionado != null) {
        var nombre by remember { mutableStateOf(entrenadorSeleccionado!!.nombre) }
        var especialidad by remember { mutableStateOf(entrenadorSeleccionado!!.especialidad) }
        var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
        var expandidoEquipos by remember { mutableStateOf(false) }

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
                entrenadorSeleccionado = null
            },
            containerColor = Color.White,
            title = {
                Text("Actualizar Entrenador", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = especialidad,
                        onValueChange = { especialidad = it },
                        label = { Text("Especialidad") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // SELECTOR DE EQUIPO
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
                        Text("Equipo", fontSize = 12.sp, color = Color.Gray)
                        Button(
                            onClick = { expandidoEquipos = !expandidoEquipos },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(equipoSeleccionado?.nombre ?: entrenadorSeleccionado!!.nombreEquipo ?: "Seleccionar equipo")
                        }

                        if (expandidoEquipos) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp)
                                    .verticalScroll(rememberScrollState())
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                    .background(Color.White)
                            ) {
                                equipos.forEach { equipo ->
                                    TextButton(
                                        onClick = {
                                            equipoSeleccionado = equipo
                                            expandidoEquipos = false
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
                                viewModel.eliminarEntrenador(entrenadorSeleccionado!!.id)
                                mostrarDialogo = false
                                entrenadorSeleccionado = null
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
                                val idEquipo = equipoSeleccionado?.id ?: entrenadorSeleccionado!!.idEquipo
                                viewModel.actualizarEntrenador(
                                    entrenadorSeleccionado!!.id,
                                    nombre,
                                    especialidad,
                                    idEquipo
                                )
                                mostrarDialogo = false
                                entrenadorSeleccionado = null
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
                                entrenadorSeleccionado = null
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