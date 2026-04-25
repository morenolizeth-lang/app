// PartidoRepository.kt - Agrega estos métodos
package com.example.applicacion.repository

import com.example.applicacion.Interfaces.RetrofitClient
import com.example.applicacion.model.Partido

class PartidoRepository {

    private val api = RetrofitClient.api

    suspend fun getPartidos(): List<Partido> {
        return api.getPartidos()
    }

    suspend fun crearPartido(
        fechadelPartido: String,
        estadio: String,
        golesLocal: Int,
        golesVisitante: Int,
        idEquipoLocal: Long,
        idEquipoVisitante: Long
    ): Partido {
        return api.crearPartido(
            Partido(
                fechadelPartido = fechadelPartido,
                estadio = estadio,
                golesLocal = golesLocal,
                golesVisitante = golesVisitante,
                idEquipoLocal = idEquipoLocal,
                idEquipoVisitante = idEquipoVisitante
            )
        )
    }

    // ✅ NUEVO - ACTUALIZAR PARTIDO
    suspend fun actualizarPartido(
        id: Long,
        fechadelPartido: String,
        estadio: String,
        golesLocal: Int,
        golesVisitante: Int,
        idEquipoLocal: Long,
        idEquipoVisitante: Long
    ): Partido {
        return api.actualizarPartido(
            id = id,
            request = Partido(
                id = id,
                fechadelPartido = fechadelPartido,
                estadio = estadio,
                golesLocal = golesLocal,
                golesVisitante = golesVisitante,
                idEquipoLocal = idEquipoLocal,
                idEquipoVisitante = idEquipoVisitante
            )
        )
    }

    // ✅ NUEVO - ELIMINAR PARTIDO
    suspend fun eliminarPartido(id: Long) {
        return api.eliminarPartido(id)
    }
}