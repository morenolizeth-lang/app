package com.example.applicacion.model

data class Entrenador(
    val id: Long = 0,
    val nombre: String,
    val especialidad: String,
    val idEquipo: Long,        // ✅ antes era equipo: Equipo
    val nombreEquipo: String? = null   // ✅ nuevo campo
)