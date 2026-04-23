package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Equipo
import com.example.applicacion.model.Jugador

class EquipoRepository {

    private val api = RetrofitClient.api

    // ✅ Desde API
    suspend fun getEquipos(): List<Equipo> {
        return api.getEquipos()
    }

    // ✅ Desde API
    suspend fun getJugadoresPorEquipo(idEquipo: Long): List<Jugador> {
        return api.getJugadoresPorEquipo(idEquipo)
    }

    // ✅ Goles calculados desde API de partidos
    suspend fun getGolesEquipo(): Map<Long, Int> {
        val partidos = api.getPartidos()
        val mapa = mutableMapOf<Long, Int>()
        partidos.forEach { partido ->
            mapa[partido.idEquipoLocal] =
                (mapa[partido.idEquipoLocal] ?: 0) + partido.golesLocal
            mapa[partido.idEquipoVisitante] =
                (mapa[partido.idEquipoVisitante] ?: 0) + partido.golesVisitante
        }
        return mapa
    }
}