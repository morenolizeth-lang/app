// EquipoViewModel.kt
package com.example.applicacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicacion.model.Equipo
import com.example.applicacion.repository.EquipoRepository
import kotlinx.coroutines.launch

class EquipoViewModel : ViewModel() {

    private val repository = EquipoRepository()

    var equipos by mutableStateOf<List<Equipo>>(emptyList())
        private set

    var golesEquipo by mutableStateOf<Map<Long, Int>>(emptyMap())
        private set

    var equipoSeleccionado by mutableStateOf<Equipo?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    init {
        cargarEquipos()
    }

    fun cargarEquipos() {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                val equiposResponse = intentar { repository.getEquipos() }
                equipos = equiposResponse

                kotlinx.coroutines.delay(500)

                val golesResponse = intentar { repository.getGolesEquipo() }
                golesEquipo = golesResponse

            } catch (e: Exception) {
                equipos = emptyList()
                golesEquipo = emptyMap()
                error = "No se pudo cargar la información. Intenta nuevamente."
            } finally {
                cargando = false
            }
        }
    }

    fun crearEquipo(nombre: String, ciudad: String, fecha: String) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                repository.crearEquipo(nombre, ciudad, fecha)
                mensaje = "Equipo creado ✔"
                cargarEquipos()
            } catch (e: Exception) {
                error = "Error al crear equipo: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // ✅ NUEVO - ACTUALIZAR
    fun actualizarEquipo(id: Long, nombre: String, ciudad: String, fecha: String) {
        viewModelScope.launch {
            cargando = true
            error = null
            mensaje = null
            try {
                repository.actualizarEquipo(id, nombre, ciudad, fecha)
                mensaje = "Equipo actualizado ✔"
                cargarEquipos()
            } catch (e: Exception) {
                error = "Error al actualizar equipo: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // ✅ NUEVO - ELIMINAR
    fun eliminarEquipo(id: Long) {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                repository.eliminarEquipo(id)
                mensaje = "Equipo eliminado ✔"
                cargarEquipos()
            } catch (e: Exception) {
                error = "Error al eliminar equipo: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    private suspend fun <T> intentar(block: suspend () -> T): T {
        repeat(3) { intento ->
            try {
                return block()
            } catch (e: Exception) {
                if (intento == 2) throw e
                kotlinx.coroutines.delay(1500)
            }
        }
        throw Exception("Error inesperado")
    }
}