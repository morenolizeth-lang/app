package com.example.applicacion.repository

import com.example.applicacion.model.Entrenador

class EntrenadorRepository {

    private val listaEntrenadores = mutableListOf(
        Entrenador(1, "Carlos Ruiz", "Principal", 1),
        Entrenador(2, "Luis Gómez", "Asistente", 1),
        Entrenador(3, "Pedro Díaz", "Preparador físico", 2)
    )

    fun getEntrenadores(): List<Entrenador> {
        return listaEntrenadores
    }
}