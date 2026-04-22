package com.example.applicacion.model

import java.util.Date

data class Partido(
    val id: Long,
    val fechadelPartido: String,
    val Estadio: String,
    val GolesLocal: Int,
    val GolesVisitante: Int,
    val idEquipoLocal: Long,      // ✅ llega del backend
    val idEquipoVisitante: Long   // ✅ llega del backend
)