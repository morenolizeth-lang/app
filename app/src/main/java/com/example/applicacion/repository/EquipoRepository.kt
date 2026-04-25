// EquipoRepository.kt
package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Equipo
import com.example.applicacion.model.Jugador

class EquipoRepository {

    private val api = RetrofitClient.api

    suspend fun getEquipos(): List<Equipo> {
        return api.getEquipos()
    }

    suspend fun getJugadoresPorEquipo(idEquipo: Long): List<Jugador> {
        return api.getJugadoresPorEquipo(idEquipo)
    }

    suspend fun getGolesEquipo(): Map<Long, Int> {
        val partidos = api.getPartidos()
        val mapa = mutableMapOf<Long, Int>()
        partidos.forEach { partido ->
            //
            mapa[partido.idEquipoLocal] =
                (mapa[partido.idEquipoLocal] ?: 0) + partido.golesLocal

            //
            mapa[partido.idEquipoVisitante] =
                (mapa[partido.idEquipoVisitante] ?: 0) + partido.golesVisitante
        }
        return mapa
    }

    //
    suspend fun crearEquipo(nombre: String, ciudad: String, fecha: String): Equipo {
        return api.crearEquipo(
            Equipo(nombre = nombre, ciudad = ciudad, fecha = fecha)
        )
    }
    suspend fun actualizarEquipo(
        id: Long,
        nombre: String,
        ciudad: String,
        fecha: String
    ): Equipo {
        return api.actualizarEquipo(
            id,
            Equipo(nombre = nombre, ciudad = ciudad, fecha = fecha)
        )
    }


    suspend fun eliminarEquipo(id: Long) {
        api.eliminarEquipo(id)
    }
}