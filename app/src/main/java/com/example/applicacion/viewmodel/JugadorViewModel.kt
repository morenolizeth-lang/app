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
    // ✅ agregar junto a los otros estados
    var errorEstadisticas: String? by mutableStateOf(null)
        private set
    private var jugadorNombreEquipoMap: Map<Long, String> = emptyMap()
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
            error = null
            try {
                jugadores = jugadorRepository.getJugadores()
                equiposCargados = equipoRepository.getEquipos()
                partidosCargados = partidoRepository.getPartidos()

                jugadorNombreEquipoMap = equiposCargados
                    .flatMap { equipo ->
                        equipo.jugadores.map { jugador -> jugador.id to equipo.nombre }
                    }
                    .toMap()
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

    // ✅ ahora usa corrutina
    fun buscarJugadoresPorGoles() {
        val golesMinimos = golesBusqueda.toIntOrNull() ?: return
        viewModelScope.launch {
            try {
                val todasStats = jugadores.flatMap { jugador ->
                    estadisticaRepository.getEstadisticasPorJugador(jugador.id)
                }
                val golesPorJugador = todasStats
                    .groupBy { it.idJugador }
                    .mapValues { (_, stats) -> stats.sumOf { it.goles } }

                jugadoresFiltrados = jugadores.filter { jugador ->
                    (golesPorJugador[jugador.id] ?: 0) >= golesMinimos
                }
                busquedaActiva = true
            } catch (e: Exception) {
                error = "Error al buscar: ${e.message}"
            }
        }
    }

    fun limpiarBusqueda() {
        golesBusqueda = ""
        jugadoresFiltrados = emptyList()
        busquedaActiva = false
    }

    fun getNombreEquipo(idJugador: Long): String {
        return jugadorNombreEquipoMap[idJugador] ?: "Desconocido"
    }

    fun getNombrePartido(idPartido: Long): String {
        val partido = partidosCargados.find { it.id == idPartido }
        return if (partido != null) {
            "${partido.nombreEquipoLocal} vs ${partido.nombreEquipoVisitante}"
        } else "Partido desconocido"
    }

    // ✅ ahora usa corrutina
    fun toggleEstadisticas(idJugador: Long) {
        if (estadisticasAbiertas.containsKey(idJugador)) {
            estadisticasAbiertas = estadisticasAbiertas - idJugador
        } else {
            viewModelScope.launch {
                try {
                    errorEstadisticas = null  // ✅ limpiar error anterior
                    val stats = estadisticaRepository.getEstadisticasPorJugador(idJugador)
                    estadisticasAbiertas = estadisticasAbiertas + (idJugador to stats)
                } catch (e: Exception) {
                    errorEstadisticas = "Error al cargar estadísticas: ${e.message}"  // ✅
                }
            }
        }
    }

}