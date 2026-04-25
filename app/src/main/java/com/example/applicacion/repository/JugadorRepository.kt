package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Jugador

class JugadorRepository {

    private val api = RetrofitClient.api

    suspend fun getJugadores(): List<Jugador> {
        return api.getTodosJugadores()
    }

    suspend fun getJugadoresPorEquipo(idEquipo: Long): List<Jugador> {
        return api.getJugadoresPorEquipo(idEquipo)
    }

    // =========================
    // CREAR JUGADOR
    // =========================
    suspend fun crearJugador(
        nombre: String,
        posicion: String,
        dorsal: Int,
        fechaNacimiento: String,
        nacionalidad: String,
        idEquipo: Long
    ): Jugador {
        return api.crearJugador(
            Jugador(
                nombre = nombre,
                posicion = posicion,
                dorsal = dorsal,
                fechaNacimiento = fechaNacimiento,
                nacionalidad = nacionalidad,
                idEquipo = idEquipo
            )
        )
    }

    // =========================
    // ACTUALIZAR JUGADOR
    // =========================
    suspend fun actualizarJugador(
        id: Long,
        nombre: String,
        posicion: String,
        dorsal: Int,
        fechaNacimiento: String,
        nacionalidad: String,
        idEquipo: Long
    ): Jugador {
        return api.actualizarJugador(
            id,
            Jugador(
                id = id,
                nombre = nombre,
                posicion = posicion,
                dorsal = dorsal,
                fechaNacimiento = fechaNacimiento,
                nacionalidad = nacionalidad,
                idEquipo = idEquipo
            )
        )
    }

    // =========================
    // ELIMINAR JUGADOR
    // =========================
    suspend fun eliminarJugador(id: Long) {
        api.eliminarJugador(id)
    }
}