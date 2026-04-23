package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Partido

class PartidoRepository {

    private val api = RetrofitClient.api

    suspend fun getPartidos(): List<Partido> {
        return api.getPartidos()  // ✅ desde API
    }

    suspend fun getPartidoPorId(id: Long): Partido {
        return api.getPartidoPorId(id)  // ✅ desde API
    }
}