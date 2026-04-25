package com.example.applicacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicacion.model.Entrenador
import com.example.applicacion.repository.EntrenadorRepository
import kotlinx.coroutines.launch

class EntrenadorViewModel : ViewModel() {

    private val repository = EntrenadorRepository()

    var entrenadores: List<Entrenador> by mutableStateOf(emptyList())
        private set

    var cargando: Boolean by mutableStateOf(false)
        private set

    var error: String? by mutableStateOf(null)
        private set

    var mensaje: String? by mutableStateOf(null)
        private set

    init {
        cargarEntrenadores()
    }

    fun cargarEntrenadores() {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                entrenadores = repository.getEntrenadores()
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun crearEntrenador(nombre: String, especialidad: String, idEquipo: Long) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                repository.crearEntrenador(nombre, especialidad, idEquipo)
                mensaje = "Entrenador creado ✔"
                cargarEntrenadores()
            } catch (e: Exception) {
                error = "Error al crear entrenador: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // ✅ ACTUALIZAR ENTRENADOR
    fun actualizarEntrenador(id: Long, nombre: String, especialidad: String, idEquipo: Long) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                repository.actualizarEntrenador(id, nombre, especialidad, idEquipo)
                mensaje = "Entrenador actualizado ✔"
                cargarEntrenadores() // refresca la lista
            } catch (e: Exception) {
                error = "Error al actualizar entrenador: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // ✅ ELIMINAR ENTRENADOR
    fun eliminarEntrenador(id: Long) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                repository.eliminarEntrenador(id)
                mensaje = "Entrenador eliminado ✔"
                cargarEntrenadores() // refresca la lista
            } catch (e: Exception) {
                error = "Error al eliminar entrenador: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }
}