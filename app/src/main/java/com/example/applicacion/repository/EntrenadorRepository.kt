package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Entrenador

class EntrenadorRepository {

    private val api = RetrofitClient.api

    suspend fun getEntrenadores(): List<Entrenador> {
        return api.getEntrenadores()
    }

    suspend fun crearEntrenador(
        nombre: String,
        especialidad: String,
        idEquipo: Long
    ): Entrenador {
        return api.crearEntrenador(
            Entrenador(
                nombre = nombre,
                especialidad = especialidad,
                idEquipo = idEquipo
            )
        )
    }

    // ✅ ACTUALIZAR ENTRENADOR
    suspend fun actualizarEntrenador(
        id: Long,
        nombre: String,
        especialidad: String,
        idEquipo: Long
    ): Entrenador {
        return api.actualizarEntrenador(
            id = id,
            request = Entrenador(
                id = id,
                nombre = nombre,
                especialidad = especialidad,
                idEquipo = idEquipo
            )
        )
    }

    // ✅ ELIMINAR ENTRENADOR
    suspend fun eliminarEntrenador(id: Long) {
        return api.eliminarEntrenador(id)
    }
}