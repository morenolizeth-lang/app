// EstadisticaJugador.kt
package com.example.applicacion.model

import com.google.gson.annotations.SerializedName

data class EstadisticaJugador(
    @SerializedName("id")
    val idEstadistica: Long = 0,
    val minutosJugados: Int = 0,
    val goles: Int = 0,
    val asistencias: Int = 0,
    @SerializedName("tarjetasAmarillas")
    val tarjetasAmarillas: Int = 0,
    @SerializedName("tarjetasRojas")
    val tarjetasRojas: Int = 0,
    val idJugador: Long = 0,
    @SerializedName("nombreJugador")
    val nombreJugador: String? = null,
    val idPartido: Long = 0
)