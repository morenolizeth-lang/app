package com.example.applicacion.model

data class Entrenador(
    val id: Long,
    val nombre: String,
    val especialidad: String,
    val idEquipo: Long,        // ✅ antes era equipo: Equipo
    val nombreEquipo: String   // ✅ nuevo campo
)