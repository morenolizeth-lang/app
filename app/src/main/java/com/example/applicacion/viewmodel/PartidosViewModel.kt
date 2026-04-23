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

    init {
        cargarPartidos()
    }

    fun cargarPartidos() {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                partidos = partidoRepository.getPartidos()  // ✅ desde API
            } catch (e: Exception) {
                error = "Error al cargar partidos: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }
}