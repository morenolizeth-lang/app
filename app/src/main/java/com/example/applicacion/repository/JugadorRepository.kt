package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Jugador

class JugadorRepository {

    private val api = RetrofitClient.api

    // ✅ Todos los jugadores desde API
    suspend fun getJugadores(): List<Jugador> {
        return api.getTodosJugadores()
    }

    // ✅ Jugadores por equipo desde API
    suspend fun getJugadoresPorEquipo(idEquipo: Long): List<Jugador> {
        return api.getJugadoresPorEquipo(idEquipo)
    }
}