package com.example.applicacion.model

data class EstadisticaJugador(
    val idEstadistica: Long,
    val idJugador: Long,
    val idPartido: Long,
    val minutosJugados: Int,
    val goles: Int,
    val asistencias: Int,
    val tarjetasAmarillas: Int,
    val tarjetasRojas: Int
)