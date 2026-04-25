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

    // ⚠️ Se mantiene pero ya no es necesario realmente
    private var jugadorNombreEquipoMap: Map<Long, String> = emptyMap()

    // ⚠️ Se mantiene pero ya no dependes de esto para mostrar equipo
    private var equiposCargados: List<Equipo> = emptyList()
    private var partidosCargados: List<Partido> = emptyList()

    var estadisticasAbiertas by mutableStateOf(mapOf<Long, List<EstadisticaJugador>>())
        private set

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            cargando = true
            try {
                val jugadoresResponse = jugadorRepository.getJugadores()
                val equiposResponse = equipoRepository.getEquipos()
                val partidosResponse = partidoRepository.getPartidos()

                jugadores = jugadoresResponse
                equiposCargados = equiposResponse
                partidosCargados = partidosResponse

            } catch (e: Exception) {
                error = "Error al cargar datos: ${e.message}"
            } finally {
                cargando = false
            }
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

    fun onGolesChange(valor: String) { golesBusqueda = valor }

    var buscando by mutableStateOf(false)
        private set

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

                if (jugadoresFiltrados.isEmpty()) {
                    error = null
                }

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

    // 🔥 MEJORA REAL AQUÍ
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
}