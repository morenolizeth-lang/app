package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.EstadisticaJugador

class EstadisticaRepository {

    private val api = RetrofitClient.api

    suspend fun getEstadisticasPorJugador(idJugador: Long): List<EstadisticaJugador> {
        return api.getEstadisticasPorJugador(idJugador)  // ✅ desde API
    }
}