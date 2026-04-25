// PartidosViewModel.kt
package com.example.applicacion.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicacion.model.Partido
import com.example.applicacion.repository.PartidoRepository
import kotlinx.coroutines.launch

class PartidosViewModel : ViewModel() {

    private val partidoRepository = PartidoRepository()

    var partidos by mutableStateOf(listOf<Partido>())
        private set

    var cargando by mutableStateOf(false)
        private set

    var error: String? by mutableStateOf(null)
        private set

    var mensaje: String? by mutableStateOf(null)
        private set

    init {
        cargarPartidos()
    }

    fun cargarPartidos() {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                partidos = partidoRepository.getPartidos()
            } catch (e: Exception) {
                error = "Error al cargar partidos: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun crearPartido(
        fechadelPartido: String,
        estadio: String,
        golesLocal: Int,
        golesVisitante: Int,
        idEquipoLocal: Long,
        idEquipoVisitante: Long
    ) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                partidoRepository.crearPartido(
                    fechadelPartido, estadio, golesLocal, golesVisitante, idEquipoLocal, idEquipoVisitante
                )
                mensaje = "Partido creado ✔"
                cargarPartidos()
            } catch (e: Exception) {
                error = "Error al crear partido: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // ✅ CORREGIDO - usa partidoRepository, no repository
    fun actualizarPartido(
        id: Long,
        fecha: String,
        estadio: String,
        golesLocal: Int,
        golesVisitante: Int,
        idEquipoLocal: Long,
        idEquipoVisitante: Long
    ) {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                partidoRepository.actualizarPartido(id, fecha, estadio, golesLocal, golesVisitante, idEquipoLocal, idEquipoVisitante)
                cargarPartidos() // refresca la lista
            } catch (e: Exception) {
                error = "Error al actualizar partido: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // ✅ CORREGIDO - usa partidoRepository, no repository
    fun eliminarPartido(id: Long) {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                partidoRepository.eliminarPartido(id)
                cargarPartidos() // refresca la lista
            } catch (e: Exception) {
                error = "Error al eliminar partido: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }
}