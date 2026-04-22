package com.example.applicacion.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.applicacion.model.EstadisticaJugador
import com.example.applicacion.model.Jugador
import com.example.applicacion.repository.*

class JugadorViewModel : ViewModel() {

    private val partidoRepository = PartidoRepository()
    private val jugadorRepository = JugadorRepository()
    private val equipoRepository = EquipoRepository()
    private val estadisticaRepository = EstadisticaRepository()

    var jugadores by mutableStateOf(listOf<Jugador>())
        private set

    var jugadoresFiltrados by mutableStateOf(listOf<Jugador>())
        private set

    var golesBusqueda by mutableStateOf("")
        private set

    var busquedaActiva by mutableStateOf(false)
        private set

    private var jugadorNombreEquipoMap: Map<Long, String> = emptyMap()

    var estadisticasAbiertas by mutableStateOf(
        mapOf<Long, List<EstadisticaJugador>>()
    )
        private set

    init {
        cargarDatos()
    }

    private fun cargarDatos() {

        jugadores = jugadorRepository.getJugadores()

        val equipos = equipoRepository.getEquipos()

        jugadorNombreEquipoMap = equipos
            .flatMap { equipo ->
                equipo.jugadores.map { jugador ->
                    jugador.id to equipo.nombre
                }
            }
            .toMap()
    }

    fun onGolesChange(valor: String) {
        golesBusqueda = valor
    }

    // 🔥 FIX APLICADO AQUÍ
    fun buscarJugadoresPorGoles() {

        val golesMinimos = golesBusqueda.toIntOrNull() ?: return

        val golesPorJugador = estadisticaRepository.getTodas()
            .groupBy { it.idJugador }
            .mapValues { (_, stats) ->
                stats.sumOf { it.goles }
            }

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

    fun getNombrePartido(idPartido: Long): String {

        val partido = partidoRepository.getPartidos()
            .find { it.id == idPartido }

        return if (partido != null) {

            val local = equipoRepository.getEquipos()
                .find { it.id == partido.idEquipoLocal }
                ?.nombre ?: "Local"

            val visitante = equipoRepository.getEquipos()
                .find { it.id == partido.idEquipoVisitante }
                ?.nombre ?: "Visitante"

            "$local vs $visitante"

        } else {
            "Partido desconocido"
        }
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