package com.example.applicacion.model

data class Equipo(
    val id: Long,
    val nombre: String,
    val ciudad: String,
    val fecha: String,
    val jugadores: List<Jugador> = emptyList()
)