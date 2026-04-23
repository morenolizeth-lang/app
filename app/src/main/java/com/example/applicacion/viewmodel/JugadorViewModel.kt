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

    private var jugadorNombreEquipoMap: Map<Long, String> = emptyMap()
    private var equiposCargados: List<Equipo> = emptyList()

    // ✅ partidos en memoria para getNombrePartido sin corrutina
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
                partidosCargados = partidoRepository.getPartidos()  // ✅ desde API

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
    fun cargaJugadores(idEquipo: Long) {
        equipoIdActual = idEquipo

        viewModelScope.launch {
            cargando = true
            error = null

            try {
                jugadores = jugadorRepository.getJugadoresPorEquipo(idEquipo)
            } catch (e: Exception) {
                error = "Error al cargar jugadores: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // 🔁 REINTENTAR
    fun reintentar() {
        equipoIdActual?.let {
            cargaJugadores(it)
        }
    }

    fun onGolesChange(valor: String) { golesBusqueda = valor }

    fun buscarJugadoresPorGoles() {
        val golesMinimos = golesBusqueda.toIntOrNull() ?: return
        val golesPorJugador = estadisticaRepository.getTodas()
            .groupBy { it.idJugador }
            .mapValues { (_, stats) -> stats.sumOf { it.goles } }
        jugadoresFiltrados = jugadores.filter { jugador ->
            (golesPorJugador[jugador.id] ?: 0) >= golesMinimos
        }
        busquedaActiva = true
    }

    fun limpiarBusqueda() {
        golesBusqueda = ""
        jugadoresFiltrados = emptyList()
        busquedaActiva = false
    }

    fun getNombreEquipo(idJugador: Long): String {
        return jugadorNombreEquipoMap[idJugador] ?: "Desconocido"
    }

    // ✅ usa partidosCargados en memoria — nombres ya vienen en el response
    fun getNombrePartido(idPartido: Long): String {
        val partido = partidosCargados.find { it.id == idPartido }
        return if (partido != null) {
            "${partido.nombreEquipoLocal} vs ${partido.nombreEquipoVisitante}"
        } else "Partido desconocido"
    }

    fun toggleEstadisticas(idJugador: Long) {
        estadisticasAbiertas = if (estadisticasAbiertas.containsKey(idJugador)) {
            estadisticasAbiertas - idJugador
        } else {
            val stats = estadisticaRepository.getEstadisticasPorJugador(idJugador)
            estadisticasAbiertas + (idJugador to stats)
        }
    }
}