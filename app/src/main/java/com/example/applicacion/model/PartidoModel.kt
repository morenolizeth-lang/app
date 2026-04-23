package com.example.applicacion.model

data class Partido(
    val id: Long,
    val fechadelPartido: String,
    val estadio: String,
    val golesLocal: Int,
    val golesVisitante: Int,
    val idEquipoLocal: Long,
    val idEquipoVisitante: Long,
    val nombreEquipoLocal: String,
    val nombreEquipoVisitante: String
)