package com.example.applicacion.model
data class Jugador(
    val id: Long = 0,                    // el backend lo ignora al crear
    val nombre: String = "",
    val posicion: String = "",
    val dorsal: Int = 0,
    val fechaNacimiento: String = "",
    val nacionalidad: String = "",
    val idEquipo: Long = 0,
    val nombreEquipo: String? = null     // el backend lo ignora al crear
)