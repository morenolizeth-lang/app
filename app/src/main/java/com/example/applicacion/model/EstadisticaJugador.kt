package com.example.applicacion.model

import com.google.gson.annotations.SerializedName

data class EstadisticaJugador(
    @SerializedName("id")
    val idEstadistica: Long,
    val minutosJugados: Int,
    val goles: Int,
    val asistencias: Int,
    @SerializedName("tarjetasAmarillas")
    val tarjetasAmarillas: Int,
    @SerializedName("tarjetasRojas")
    val tarjetasRojas: Int,
    val idJugador: Long,
    @SerializedName("nombreJugador")
    val nombreJugador: String = "",  // ✅ llega del backend pero no se muestra
    val idPartido: Long
)