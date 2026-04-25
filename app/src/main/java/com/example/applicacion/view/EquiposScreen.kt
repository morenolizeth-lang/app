package com.example.applicacion.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.applicacion.R
import com.example.applicacion.model.Equipo
import com.example.applicacion.viewmodel.EquipoViewModel
import com.example.applicacion.viewmodel.JugadorViewModel
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
    val cargando = viewModel.cargando
    val error = viewModel.error

    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }        // ✅ dialogo eliminar/actualizar
    var mostrarFormulario by remember { mutableStateOf(false) }     // ✅ formulario actualizar
    var mostrarConfirmarEliminar by remember { mutableStateOf(false) }
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

        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Red
            )
        }

        if (error != null) {
            Text(text = error, color = Color.Red, modifier = Modifier.padding(8.dp))
            Button(
                onClick = { viewModel.cargarEquipos() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
            ) {
                Text("Reintentar")
            }
            return@Column
        }

        if (equipos.isEmpty() && !cargando) {
            Text(text = "No hay equipos registrados.", color = Color.Gray, modifier = Modifier.padding(8.dp))
        }

        if (!cargando && error == null) {
            equipos.forEach { equipo ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Black)),
                            shape = RoundedCornerShape(16.dp)
                        )
                        // ✅ toque en cualquier parte del card abre el dialogo
                        .clickable {
                            equipoSeleccionado = equipo
                            mostrarDialogo = true
                            mostrarFormulario = false
                        },
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
                                Text(equipo.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.linearGradient(listOf(Color.Red, Color.Black)),
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

                        // ✅ botón jugadores no cambia
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(colors = listOf(Color.Red, Color.Black))
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
                                Text(text = "Jugadores", modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }

        // ✅ FORMULARIO DE ACTUALIZAR aparece debajo del listado
        if (mostrarFormulario && equipoSeleccionado != null) {
            Spacer(modifier = Modifier.height(16.dp))
            FormularioActualizarEquipo(
                equipo = equipoSeleccionado!!,
                viewModel = viewModel,
                onCerrar = {
                    mostrarFormulario = false
                    equipoSeleccionado = null
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar")
        }
    }

    // ✅ DIALOGO EMERGENTE — eliminar o actualizar
    if (mostrarDialogo && equipoSeleccionado != null) {

        var nombre by remember { mutableStateOf(equipoSeleccionado!!.nombre) }
        var ciudad by remember { mutableStateOf(equipoSeleccionado!!.ciudad) }
        var fecha by remember { mutableStateOf(equipoSeleccionado!!.fecha) }


        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Actualizar Equipo",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Column {

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = ciudad,
                        onValueChange = { ciudad = it },
                        label = { Text("Ciudad") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha (yyyy-MM-dd)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {

                // 🔥 FILA: ELIMINAR + GUARDAR (rectangulares unidos visualmente)
                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {

                        Button(
                            onClick = {
                                mostrarConfirmarEliminar = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Eliminar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                viewModel.actualizarEquipo(
                                    equipoSeleccionado!!.id,
                                    nombre,
                                    ciudad,
                                    fecha
                                )
                                mostrarFormulario = false
                                equipoSeleccionado = null
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

                    Spacer(modifier = Modifier.height(10.dp))

                    // 🔥 CANCELAR CENTRADO ABAJO
                    TextButton(onClick = {
                        mostrarDialogo = false
                        equipoSeleccionado = null
                    }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                }
            },
            dismissButton = {}
        )
    }
    if (mostrarConfirmarEliminar && equipoSeleccionado != null) {

        AlertDialog(
            onDismissRequest = { mostrarConfirmarEliminar = false },
            title = {
                Text("Confirmar eliminación", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("¿Seguro que quieres eliminar este equipo?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarEquipo(equipoSeleccionado!!.id)
                        mostrarConfirmarEliminar = false
                        mostrarDialogo = false
                        equipoSeleccionado = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Sí, eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarConfirmarEliminar = false
                }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

// ✅ FORMULARIO ACTUALIZAR EQUIPO con datos precargados
@Composable
fun FormularioActualizarEquipo(
    equipo: Equipo,
    viewModel: EquipoViewModel,
    onCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf(equipo.nombre) }      // ✅ datos actuales
    var ciudad by remember { mutableStateOf(equipo.ciudad) }
    var fecha by remember { mutableStateOf(equipo.fecha) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Red, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("Actualizar Equipo", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(ciudad, { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(fecha, { fecha = it }, label = { Text("Fecha (yyyy-MM-dd)") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    viewModel.actualizarEquipo(equipo.id, nombre, ciudad, fecha)
                    onCerrar()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Guardar", color = Color.White)
            }
            Button(
                onClick = onCerrar,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Cancelar", color = Color.White)
            }
        }

        viewModel.mensaje?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
        viewModel.error?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp)) }
    }
}