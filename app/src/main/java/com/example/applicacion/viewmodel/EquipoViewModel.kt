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

    init {
        cargarEquipos()
    }

    fun cargarEquipos() {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                equipos = repository.getEquipos()
                golesEquipo = repository.getGolesEquipo()
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun seleccionarYAbrirJugadores(equipo: Equipo) {
        equipoSeleccionado = equipo
    }
}