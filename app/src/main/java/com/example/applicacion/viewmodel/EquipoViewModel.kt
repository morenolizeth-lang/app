package com.example.applicacion.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.applicacion.model.Equipo
import com.example.applicacion.model.Jugador
import com.example.applicacion.model.Entrenador
import com.example.applicacion.repository.*

class EquipoViewModel : ViewModel() {

    private val entrenadorRepository = EntrenadorRepository()
    private val equipoRepository = EquipoRepository()
    private val jugadorRepository = JugadorRepository()
    private val partidoRepository = PartidoRepository()

    var jugadores by mutableStateOf(listOf<Jugador>())
        private set

    var equipos by mutableStateOf(listOf<Equipo>())
        private set

    var entrenadores by mutableStateOf(listOf<Entrenador>())
        private set

    var golesEquipo by mutableStateOf(mapOf<Long, Int>())
        private set

    init {
        cargarEquipos()
        cargarEntrenadores()
    }

    fun cargarEquipos() {

        equipos = equipoRepository.getEquipos()

        val partidos = partidoRepository.getPartidos()

        golesEquipo = equipos.associate { equipo ->

            val golesLocal = partidos
                .filter { it.idEquipoLocal == equipo.id }
                .sumOf { it.GolesLocal }

            val golesVisitante = partidos
                .filter { it.idEquipoVisitante == equipo.id }
                .sumOf { it.GolesVisitante }

            equipo.id to (golesLocal + golesVisitante)
        }
    }

    fun cargarEntrenadores() {
        entrenadores = entrenadorRepository.getEntrenadores()
    }

    fun seleccionarYAbrirJugadores(equipo: Equipo) {
        cargarJugadoresPorEquipo(equipo.id)
    }

    fun cargarJugadoresPorEquipo(idEquipo: Long) {
        jugadores = jugadorRepository.getJugadoresPorEquipo(idEquipo)
    }
}