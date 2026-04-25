// EstadisticaRepository.kt
package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.EstadisticaJugador

class EstadisticaRepository {

    private val api = RetrofitClient.api

    suspend fun getEstadisticasPorJugador(idJugador: Long): List<EstadisticaJugador> {
        return api.getEstadisticasPorJugador(idJugador)
    }

    // ✅ NUEVO
    suspend fun crearEstadisticas(
        idJugador: Long,
        idPartido: Long,
        minutos: Int,
        goles: Int,
        asistencias: Int,
        amarillas: Int,
        rojas: Int
    ): EstadisticaJugador {
        return api.crearEstadisticas(
            EstadisticaJugador(
                idJugador = idJugador,
                idPartido = idPartido,
                minutosJugados = minutos,
                goles = goles,
                asistencias = asistencias,
                tarjetasAmarillas = amarillas,
                tarjetasRojas = rojas
            )
        )
    }
}