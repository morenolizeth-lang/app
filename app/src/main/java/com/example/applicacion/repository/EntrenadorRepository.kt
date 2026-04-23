package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Entrenador

class EntrenadorRepository {

    private val api = RetrofitClient.api

    suspend fun getEntrenadores(): List<Entrenador> {
        return api.getEntrenadores()
    }
}