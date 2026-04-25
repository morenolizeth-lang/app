// Partido.kt
package com.example.applicacion.model

data class Partido(
    val id: Long = 0,
    val fechadelPartido: String = "",
    val estadio: String = "",
    val golesLocal: Int = 0,
    val golesVisitante: Int = 0,
    val idEquipoLocal: Long = 0,
    val idEquipoVisitante: Long = 0,
    val nombreEquipoLocal: String? = null,
    val nombreEquipoVisitante: String? = null
)