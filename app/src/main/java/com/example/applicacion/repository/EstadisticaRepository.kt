package com.example.applicacion.repository

import com.example.applicacion.model.EstadisticaJugador

class EstadisticaRepository {

    private val lista = mutableListOf(
        EstadisticaJugador(1, 1, 1,  90, 2, 1, 0, 0),
        EstadisticaJugador(2, 1, 2,    75, 1, 0, 1, 0),
        EstadisticaJugador(3, 2, 1, 90, 0, 0, 0, 0),
        EstadisticaJugador(4, 2, 2,     60, 0, 1, 1, 0),
        EstadisticaJugador(5, 3, 1,  85, 1, 2, 0, 0),
        EstadisticaJugador(6, 3, 2,     90, 0, 1, 0, 1)
    )

    fun getEstadisticasPorJugador(idJugador: Long): List<EstadisticaJugador> {
        return lista.filter { it.idJugador == idJugador }
    }

    fun getTodas(): List<EstadisticaJugador> {
        return lista
    }
}