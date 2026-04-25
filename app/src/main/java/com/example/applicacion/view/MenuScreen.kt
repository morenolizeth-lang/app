package com.example.applicacion.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.applicacion.R
import com.example.applicacion.model.Equipo
import com.example.applicacion.repository.EstadisticaRepository
import com.example.applicacion.repository.JugadorRepository
import com.example.applicacion.repository.PartidoRepository
import com.example.applicacion.ui.theme.ApplicacionTheme
import com.example.applicacion.viewmodel.EntrenadorViewModel
import com.example.applicacion.viewmodel.EquipoViewModel
import com.example.applicacion.viewmodel.JugadorViewModel
import com.example.applicacion.viewmodel.PartidosViewModel
import kotlinx.coroutines.launch

// =======================
// SCREEN PRINCIPAL
// =======================
@Composable
fun MenuScreen(navController: NavController) {

    var tipoFormulario by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())  // ✅ toda la pantalla scrolleable
            .padding(16.dp)
    ) {
        HeaderPortada()

        Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ BOTÓN AGREGAR CON DROPDOWN
            BotonAgregar(onSeleccion = { tipoFormulario = it })

            Spacer(modifier = Modifier.height(15.dp))
            if (tipoFormulario != null) {
                TextButton(
                    onClick = { tipoFormulario = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✕ Cerrar formulario", color = Color.Gray)
                }
            }
            // ✅ FORMULARIO APARECE DEBAJO AL SELECCIONAR
            when (tipoFormulario) {
                "equipo"     -> FormularioEquipo()
                "jugador"    -> FormularioJugadorConEstadisticas()
                "entrenador" -> FormularioEntrenador()
                "partido"    -> FormularioPartido()
            }


        }
    }
}

// =======================
// BOTÓN REUTILIZABLE
// =======================
@Composable
fun BotonMenu(texto: String, icono: Int, onClick: () -> Unit) {
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
            Text(text = texto, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// =======================
// HEADER
// =======================
@Composable
fun HeaderPortada() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Black)),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(R.drawable.deporte),
            contentDescription = "deporte",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
        )
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
                    brush = Brush.linearGradient(colors = listOf(Color.Red, Color.Black))
                )
        )
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

// =======================
// BOTÓN AGREGAR
// =======================
@Composable
fun BotonAgregar(onSeleccion: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { expanded = !expanded },  // ✅ toggle
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            border = BorderStroke(
                2.dp,
                Brush.linearGradient(listOf(Color.Red, Color.Black))
            )
        ) {
            Text(if (expanded) "▲ Agregar" else "▼ Agregar")  // ✅ indica si está abierto
        }

        // ✅ LISTA DEBAJO DEL BOTÓN EN EL FLUJO NORMAL
        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                listOf(
                    "equipo"     to "Agregar equipo",
                    "jugador"    to "Agregar jugador",
                    "entrenador" to "Agregar entrenador",
                    "partido"    to "Agregar partido"
                ).forEach { (tipo, texto) ->
                    TextButton(
                        onClick = {
                            expanded = false       // ✅ cierra la lista
                            onSeleccion(tipo)      // ✅ muestra el formulario
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = texto,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    HorizontalDivider(color = Color.LightGray)
                }
            }
        }
    }
}

// =======================
// FORMULARIO EQUIPO
// =======================
@Composable
fun FormularioEquipo(viewModel: EquipoViewModel = viewModel()) {

    var nombre by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, Color.Red, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text("Agregar Equipo", fontWeight = FontWeight.Bold)

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(ciudad, { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(fecha, { fecha = it }, label = { Text("Fecha (yyyy-MM-dd)") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { viewModel.crearEquipo(nombre, ciudad, fecha) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Guardar", color = Color.White)
        }

        viewModel.mensaje?.let { Text(it) }
        viewModel.error?.let { Text(it, color = Color.Red) }
    }
}

// =======================
// FORMULARIO ENTRENADOR
// =======================
@Composable
fun FormularioEntrenador(viewModel: EntrenadorViewModel = viewModel()) {

    var nombre by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var expandidoEquipos by remember { mutableStateOf(false) }

    val equipoViewModel: EquipoViewModel = viewModel()
    val equipos = equipoViewModel.equipos

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, Color.Red, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text("Agregar Entrenador", fontWeight = FontWeight.Bold)

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(especialidad, { especialidad = it }, label = { Text("Especialidad") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ SELECTOR DE EQUIPO
        Text("Equipo", fontSize = 12.sp, color = Color.Gray)
        Button(
            onClick = { expandidoEquipos = !expandidoEquipos },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(equipoSeleccionado?.nombre ?: "Seleccionar equipo")
        }

        if (expandidoEquipos) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val id = equipoSeleccionado?.id ?: return@Button
                viewModel.crearEntrenador(nombre, especialidad, id)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Guardar", color = Color.White)
        }

        viewModel.mensaje?.let { Text(it) }
        viewModel.error?.let { Text(it, color = Color.Red) }
    }
}

@Composable
fun FormularioPartido() {

    var fechadelPartido by remember { mutableStateOf("") }
    var estadio by remember { mutableStateOf("") }
    var golesLocal by remember { mutableStateOf("") }
    var golesVisitante by remember { mutableStateOf("") }
    var equipoLocalSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var equipoVisitanteSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var expandidoLocal by remember { mutableStateOf(false) }
    var expandidoVisitante by remember { mutableStateOf(false) }
    var mensajeLocal by remember { mutableStateOf<String?>(null) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    // ✅ solo EquipoViewModel para cargar la lista de equipos
    val equipoViewModel: EquipoViewModel = viewModel()
    val equipos = equipoViewModel.equipos

    // ✅ repositorio directo, sin ViewModel que cargue partidos innecesarios
    val partidoRepository = remember { PartidoRepository() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, Color.Red, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text("Agregar Partido", fontWeight = FontWeight.Bold)

        OutlinedTextField(fechadelPartido, { fechadelPartido = it }, label = { Text("Fecha (yyyy-MM-dd)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(estadio, { estadio = it }, label = { Text("Estadio") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(golesLocal, { golesLocal = it }, label = { Text("Goles Local") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(golesVisitante, { golesVisitante = it }, label = { Text("Goles Visitante") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ SELECTOR EQUIPO LOCAL
        Text("Equipo Local", fontSize = 12.sp, color = Color.Gray)
        Button(
            onClick = { expandidoLocal = !expandidoLocal },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(equipoLocalSeleccionado?.nombre ?: "Seleccionar equipo local")
        }

        if (expandidoLocal) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ SELECTOR EQUIPO VISITANTE
        Text("Equipo Visitante", fontSize = 12.sp, color = Color.Gray)
        Button(
            onClick = { expandidoVisitante = !expandidoVisitante },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(equipoVisitanteSeleccionado?.nombre ?: "Seleccionar equipo visitante")
        }

        if (expandidoVisitante) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val gl = golesLocal.toIntOrNull() ?: return@Button
                val gv = golesVisitante.toIntOrNull() ?: return@Button
                val local = equipoLocalSeleccionado?.id ?: return@Button
                val visitante = equipoVisitanteSeleccionado?.id ?: return@Button
                scope.launch {
                    try {
                        errorLocal = null
                        mensajeLocal = null
                        partidoRepository.crearPartido(
                            fechadelPartido, estadio, gl, gv, local, visitante
                        )
                        mensajeLocal = "Partido creado ✔"
                    } catch (e: Exception) {
                        errorLocal = "Error al crear partido: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Guardar", color = Color.White)
        }

        mensajeLocal?.let { Text(it, modifier = Modifier.padding(8.dp)) }
        errorLocal?.let { Text(it, color = Color.Red, modifier = Modifier.padding(8.dp)) }
    }
}

@Composable
fun FormularioJugadorConEstadisticas(
    estadisticaRepository: EstadisticaRepository = EstadisticaRepository()
) {
    var nombre by remember { mutableStateOf("") }
    var posicion by remember { mutableStateOf("") }
    var dorsal by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var expandidoEquipos by remember { mutableStateOf(false) }
    var mensajeLocal by remember { mutableStateOf<String?>(null) }
    var errorLocal by remember { mutableStateOf<String?>(null) }
    var jugadorCreadoId by remember { mutableStateOf<Long?>(null) }

    val equipoViewModel: EquipoViewModel = viewModel()
    val equipos = equipoViewModel.equipos

    // ✅ repositorio directo, sin ViewModel que cargue datos innecesarios
    val jugadorRepository = remember { JugadorRepository() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, Color.Red, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text("Agregar Jugador", fontWeight = FontWeight.Bold)

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(posicion, { posicion = it }, label = { Text("Posición") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(dorsal, { dorsal = it }, label = { Text("Dorsal") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(fechaNacimiento, { fechaNacimiento = it }, label = { Text("Fecha (yyyy-MM-dd)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(nacionalidad, { nacionalidad = it }, label = { Text("Nacionalidad") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        Text("Equipo", fontSize = 12.sp, color = Color.Gray)
        Button(
            onClick = { expandidoEquipos = !expandidoEquipos },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(equipoSeleccionado?.nombre ?: "Seleccionar equipo")
        }

        if (expandidoEquipos) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val dorsalInt = dorsal.toIntOrNull() ?: return@Button
                val equipoId = equipoSeleccionado?.id ?: return@Button
                scope.launch {
                    try {
                        errorLocal = null
                        mensajeLocal = null
                        // ✅ llama directo al repositorio sin cargar datos extra
                        val jugador = jugadorRepository.crearJugador(
                            nombre, posicion, dorsalInt, fechaNacimiento, nacionalidad, equipoId
                        )
                        jugadorCreadoId = jugador.id
                        mensajeLocal = "Jugador creado ✔ Ahora agrega estadísticas"
                    } catch (e: Exception) {
                        errorLocal = "Error al crear jugador: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Guardar Jugador", color = Color.White)
        }

        mensajeLocal?.let { Text(it, modifier = Modifier.padding(8.dp)) }
        errorLocal?.let { Text(it, color = Color.Red, modifier = Modifier.padding(8.dp)) }

        jugadorCreadoId?.let { idJugador ->
            Spacer(modifier = Modifier.height(20.dp))
            FormularioEstadisticasJugador(
                idJugador = idJugador,
                estadisticaRepository = estadisticaRepository
            )
        }
    }
}
// =======================
// FORMULARIO ESTADÍSTICAS
// =======================
@Composable
fun FormularioEstadisticasJugador(
    idJugador: Long,
    estadisticaRepository: EstadisticaRepository
) {
    var idPartido by remember { mutableStateOf("") }
    var minutos by remember { mutableStateOf("") }
    var goles by remember { mutableStateOf("") }
    var asistencias by remember { mutableStateOf("") }
    var amarillas by remember { mutableStateOf("") }
    var rojas by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text("Estadísticas del Jugador ID: $idJugador", fontWeight = FontWeight.Bold)

        OutlinedTextField(idPartido, { idPartido = it }, label = { Text("ID Partido") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(minutos, { minutos = it }, label = { Text("Minutos") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(goles, { goles = it }, label = { Text("Goles") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(asistencias, { asistencias = it }, label = { Text("Asistencias") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(amarillas, { amarillas = it }, label = { Text("Amarillas") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(rojas, { rojas = it }, label = { Text("Rojas") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val partido = idPartido.toLongOrNull() ?: return@Button
                scope.launch {
                    try {
                        estadisticaRepository.crearEstadisticas(
                            idJugador = idJugador,
                            idPartido = partido,
                            minutos = minutos.toIntOrNull() ?: 0,
                            goles = goles.toIntOrNull() ?: 0,
                            asistencias = asistencias.toIntOrNull() ?: 0,
                            amarillas = amarillas.toIntOrNull() ?: 0,
                            rojas = rojas.toIntOrNull() ?: 0
                        )
                        mensaje = "Estadísticas guardadas ✔"
                    } catch (e: Exception) {
                        mensaje = "Error al guardar estadísticas"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Guardar Estadísticas", color = Color.White)
        }

        mensaje?.let { Text(it, modifier = Modifier.padding(8.dp)) }
    }
}

// =======================
// PREVIEW
// =======================
@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    ApplicacionTheme {
        MenuScreen(navController = rememberNavController())
    }
}