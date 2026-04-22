package com.example.applicacion.repository

import com.example.applicacion.model.Equipo
import com.example.applicacion.model.Jugador

class EquipoRepository {

    private val listaEquipos = listOf(
        Equipo(
            1L,
            "Nacional",
            "Medellín",
            "2020",
            jugadores = listOf(
                Jugador(1L, "James Rodríguez", "Mediocampista", 10, "1991-07-12", "Colombiana")
            )
        ),
        Equipo(
            2L,
            "Millonarios",
            "Bogotá",
            "2020",
            jugadores = listOf(
                Jugador(2L, "David Ospina", "Portero", 1, "1988-08-31", "Colombiana")
            )
        ),
        Equipo(
            3L,
            "Santa Fe",
            "Bogotá",
            "2020",
            jugadores = listOf(
                Jugador(3L, "Juan Cuadrado", "Lateral", 11, "1988-05-26", "Colombiana")
            )
        )
    )

    fun getEquipos(): List<Equipo> = listaEquipos
}