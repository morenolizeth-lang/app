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
                // 🔥 1. cargar equipos primero
                val equiposResponse = intentar { repository.getEquipos() }
                equipos = equiposResponse

                // 🔥 2. pequeño delay (evita saturar backend)
                kotlinx.coroutines.delay(500)

                // 🔥 3. cargar goles después
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

    // 🔁 FUNCIÓN DE REINTENTO AUTOMÁTICO
    private suspend fun <T> intentar(block: suspend () -> T): T {
        repeat(3) { intento ->
            try {
                return block()
            } catch (e: Exception) {
                if (intento == 2) throw e
                kotlinx.coroutines.delay(1500) // 🔥 espera antes de reintentar
            }
        }
        throw Exception("Error inesperado")
    }
}