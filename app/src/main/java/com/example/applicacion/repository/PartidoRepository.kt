package com.example.applicacion.repository

import com.example.applicacion.model.Partido

class PartidoRepository {

    private val listaPartidos = listOf(
        Partido(1L, "2024-03-12", "Atanasio Girardot", 2, 1, 1L, 3L),
        Partido(2L, "2024-03-15", "El Campín", 0, 0, 1L, 2L),
        Partido(3L, "2024-03-20", "El Campín", 1, 2, 2L, 3L)
    )

    fun getPartidos(): List<Partido> {
        return listaPartidos
    }

    fun getPartidosPorEquipo(idEquipo: Long): List<Partido> {
        return listaPartidos.filter {
            it.idEquipoLocal == idEquipo || it.idEquipoVisitante == idEquipo
        }
    }
}