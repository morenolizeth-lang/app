package com.example.applicacion.network

import com.example.applicacion.model.Jugador
import com.example.applicacion.model.Equipo
import com.example.applicacion.model.Entrenador
import com.example.applicacion.model.Partido
import com.example.applicacion.model.EstadisticaJugador
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // ========== EQUIPOS ==========
    // GET /api/Equipo
    @GET("api/Equipo")
    suspend fun getEquipos(): List<Equipo>

    // GET /api/Equipo/{id}
    @GET("api/Equipo/{id}")
    suspend fun getEquipoPorId(
        @Path("id") id: Long
    ): Equipo

    // ========== JUGADORES ==========
    // GET /api/Jugador
    @GET("api/Jugador")
    suspend fun getTodosJugadores(): List<Jugador>

    // GET /api/Jugador/{id}
    @GET("api/Jugador/{id}")
    suspend fun getJugadorPorId(
        @Path("id") id: Long
    ): Jugador

    // GET /api/Jugador/goles/{goles}
    @GET("api/Jugador/goles/{goles}")
    suspend fun jugadoresConMasGoles(
        @Path("goles") goles: Int
    ): List<Jugador>

    // GET /api/Jugador/equipo/{id}
    @GET("api/Jugador/equipo/{id}")
    suspend fun getJugadoresPorEquipo(
        @Path("id") idEquipo: Long
    ): List<Jugador>

    // ========== ENTRENADORES ==========
    // GET /api/Entrenador
    @GET("api/Entrenador")
    suspend fun getEntrenadores(): List<Entrenador>

    // GET /api/Entrenador/{id}
    @GET("api/Entrenador/{id}")
    suspend fun getEntrenadorPorId(
        @Path("id") id: Long
    ): Entrenador

    // ========== PARTIDOS ==========
    // GET /api/Partido
    @GET("api/Partido")
    suspend fun getPartidos(): List<Partido>

    // GET /api/Partido/{id}
    @GET("api/Partido/{id}")
    suspend fun getPartidoPorId(
        @Path("id") id: Long
    ): Partido

    // GET /api/Partido/equipos/{id}/goles
    @GET("api/Partido/equipos/{id}/goles")
    suspend fun getTotalGolesEquipo(
        @Path("id") idEquipo: Long
    ): Int

    // GET /api/Partido/resultados
    @GET("api/Partido/resultados")
    suspend fun getResultados(): List<Any>

    // ========== ESTADÍSTICAS ==========
    // GET /api/Estadisticas/{id}
    @GET("api/Estadisticas/{id}")
    suspend fun getEstadisticaPorId(
        @Path("id") id: Long
    ): EstadisticaJugador
    @GET("api/Estadisticas/jugador/{id}")
    suspend fun getEstadisticasPorJugador(
        @Path("id") idJugador: Long
    ): List<EstadisticaJugador>
}