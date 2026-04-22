package com.example.applicacion.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.applicacion.model.Partido
import com.example.applicacion.repository.EquipoRepository
import com.example.applicacion.repository.PartidoRepository

class PartidosViewModel : ViewModel() {

    private val equipoRepository = EquipoRepository()
    private val partidoRepository = PartidoRepository()

    data class PartidoConEquipos(
        val partido: Partido,
        val nombreLocal: String,
        val nombreVisitante: String
    )

    var partidos by mutableStateOf(listOf<PartidoConEquipos>())
        private set

    init {
        cargarPartidos()
    }

    private fun cargarPartidos() {

        val equipos = equipoRepository.getEquipos()
        val listaPartidos = partidoRepository.getPartidos()

        val lista = mutableListOf<PartidoConEquipos>()

        listaPartidos.forEach { partido ->

            val equipoLocal = equipos.find {
                it.id == partido.idEquipoLocal
            }

            val equipoVisitante = equipos.find {
                it.id == partido.idEquipoVisitante
            }

            lista.add(
                PartidoConEquipos(
                    partido = partido,
                    nombreLocal = equipoLocal?.nombre ?: "Desconocido",
                    nombreVisitante = equipoVisitante?.nombre ?: "Desconocido"
                )
            )
        }

        partidos = lista.distinctBy { it.partido.id }
    }
}