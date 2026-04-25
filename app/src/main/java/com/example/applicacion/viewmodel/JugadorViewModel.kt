// JugadorViewModel.kt
package com.example.applicacion.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicacion.model.Equipo
import com.example.applicacion.model.EstadisticaJugador
import com.example.applicacion.model.Jugador
import com.example.applicacion.model.Partido
import com.example.applicacion.repository.*
import kotlinx.coroutines.launch

class JugadorViewModel : ViewModel() {

    private val jugadorRepository = JugadorRepository()
    private val equipoRepository = EquipoRepository()
    private val estadisticaRepository = EstadisticaRepository()
    private val partidoRepository = PartidoRepository()

    private var equipoIdActual: Long? = null

    var jugadores by mutableStateOf(listOf<Jugador>())
        private set

    var jugadoresFiltrados by mutableStateOf(listOf<Jugador>())
        private set

    var golesBusqueda by mutableStateOf("")
        private set

    var busquedaActiva by mutableStateOf(false)
        private set

    var cargando by mutableStateOf(false)
        private set

    var error: String? by mutableStateOf(null)
        private set

    var errorEstadisticas: String? by mutableStateOf(null)
        private set

    var mensaje: String? by mutableStateOf(null)  // ✅ NUEVO
        private set

    private var equiposCargados: List<Equipo> = emptyList()
    private var partidosCargados: List<Partido> = emptyList()

    var estadisticasAbiertas by mutableStateOf(mapOf<Long, List<EstadisticaJugador>>())
        private set

    var buscando by mutableStateOf(false)
        private set
    var jugadorCreadoId: Long? by mutableStateOf(null)  // ✅ AGREGA ESTO
        private set
    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                jugadores = jugadorRepository.getJugadores()
            } catch (e: Exception) {
                error = "Error al cargar jugadores: ${e.message}"
            }

            try {
                equiposCargados = equipoRepository.getEquipos()
            } catch (e: Exception) {
                // no muestra error, los equipos se cargan en EquipoViewModel
            }

            try {
                partidosCargados = partidoRepository.getPartidos()
            } catch (e: Exception) {
                // no muestra error si no hay partidos aún
                partidosCargados = emptyList()
            }

            cargando = false
        }
    }

    fun cargarJugadores(equipo: Equipo) {
        equipoIdActual = equipo.id
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                jugadores = jugadorRepository.getJugadoresPorEquipo(equipo.id)
                jugadoresFiltrados = emptyList()
                busquedaActiva = false
                golesBusqueda = ""
                estadisticasAbiertas = emptyMap()
            } catch (e: Exception) {
                error = "Error al cargar jugadores: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun cargarJugadores() {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                jugadores = jugadorRepository.getJugadores()
                jugadoresFiltrados = emptyList()
                busquedaActiva = false
                golesBusqueda = ""
                estadisticasAbiertas = emptyMap()
            } catch (e: Exception) {
                error = "Error al cargar jugadores: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun cargarJugadoresPorEquipo(idEquipo: Long) {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                jugadores = jugadorRepository.getJugadoresPorEquipo(idEquipo)
                jugadoresFiltrados = emptyList()
                busquedaActiva = false
                golesBusqueda = ""
                estadisticasAbiertas = emptyMap()
            } catch (e: Exception) {
                error = "Error al cargar jugadores: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // ✅ NUEVO
    fun crearJugador(
        nombre: String,
        posicion: String,
        dorsal: Int,
        fechaNacimiento: String,
        nacionalidad: String,
        idEquipo: Long
    ) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            jugadorCreadoId = null  // ✅ resetea antes de crear
            try {
                val jugador = jugadorRepository.crearJugador(
                    nombre, posicion, dorsal, fechaNacimiento, nacionalidad, idEquipo
                )
                jugadorCreadoId = jugador.id  // ✅ guarda el id del jugador creado
                mensaje = "Jugador creado ✔ Ahora agrega estadísticas"
                cargarJugadores()
            } catch (e: Exception) {
                error = "Error al crear jugador: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun onGolesChange(valor: String) { golesBusqueda = valor }

    fun buscarJugadoresPorGoles() {
        val golesMinimos = golesBusqueda.toIntOrNull() ?: return
        if (buscando) return
        buscando = true
        error = null
        viewModelScope.launch {
            try {
                val todasStats = mutableListOf<EstadisticaJugador>()
                for (jugador in jugadores) {
                    try {
                        val stats = estadisticaRepository.getEstadisticasPorJugador(jugador.id)
                        todasStats.addAll(stats)
                    } catch (_: Exception) { }
                }
                val golesPorJugador = todasStats
                    .groupBy { it.idJugador }
                    .mapValues { (_, stats) -> stats.sumOf { it.goles } }
                jugadoresFiltrados = jugadores.filter { jugador ->
                    (golesPorJugador[jugador.id] ?: 0) >= golesMinimos
                }
                busquedaActiva = true
            } catch (e: Exception) {
                jugadoresFiltrados = emptyList()
                busquedaActiva = true
            } finally {
                buscando = false
            }
        }
    }

    fun limpiarBusqueda() {
        golesBusqueda = ""
        jugadoresFiltrados = emptyList()
        busquedaActiva = false
    }

    fun getNombreEquipo(jugador: Jugador): String {
        return jugador.nombreEquipo ?: "Sin equipo"
    }

    fun getNombrePartido(idPartido: Long): String {
        val partido = partidosCargados.find { it.id == idPartido }
        return if (partido != null) {
            "${partido.nombreEquipoLocal} vs ${partido.nombreEquipoVisitante}"
        } else "Partido desconocido"
    }

    fun toggleEstadisticas(idJugador: Long) {
        if (estadisticasAbiertas.containsKey(idJugador)) {
            estadisticasAbiertas = estadisticasAbiertas - idJugador
        } else {
            viewModelScope.launch {
                try {
                    errorEstadisticas = null
                    val stats = estadisticaRepository.getEstadisticasPorJugador(idJugador)
                    estadisticasAbiertas = estadisticasAbiertas + (idJugador to stats)
                } catch (e: Exception) {
                    errorEstadisticas = "Error al cargar estadísticas: ${e.message}"
                }
            }
        }
    }
    fun actualizarJugador(
        id: Long,
        nombre: String,
        posicion: String,
        dorsal: Int,
        fechaNacimiento: String,
        nacionalidad: String,
        idEquipo: Long
    ) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                jugadorRepository.actualizarJugador(
                    id,
                    nombre,
                    posicion,
                    dorsal,
                    fechaNacimiento,
                    nacionalidad,
                    idEquipo
                )
                mensaje = "Jugador actualizado ✔"
                cargarJugadores()
            } catch (e: Exception) {
                error = "Error al actualizar jugador: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }
    fun eliminarJugador(id: Long) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                jugadorRepository.eliminarJugador(id)
                mensaje = "Jugador eliminado ✔"
                cargarJugadores()
            } catch (e: Exception) {
                error = "Error al eliminar jugador: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }
}