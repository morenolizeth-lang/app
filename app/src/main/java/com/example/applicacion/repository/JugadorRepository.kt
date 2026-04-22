package com.example.applicacion.repository

import com.example.applicacion.model.Jugador

class JugadorRepository {

    private val equipoRepository = EquipoRepository()

    // ✅ Todos los jugadores vienen de los equipos
    fun getJugadores(): List<Jugador> {
        return equipoRepository.getEquipos().flatMap { it.jugadores }
    }

    // ✅ Filtra por equipo correctamente
    fun getJugadoresPorEquipo(idEquipo: Long): List<Jugador> {
        return equipoRepository.getEquipos()
            .find { it.id == idEquipo }
            ?.jugadores ?: emptyList()
    }
}