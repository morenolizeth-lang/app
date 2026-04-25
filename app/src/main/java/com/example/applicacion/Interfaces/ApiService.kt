package com.example.applicacion.network

import com.example.applicacion.model.Jugador
import com.example.applicacion.model.Equipo
import com.example.applicacion.model.Entrenador
import com.example.applicacion.model.Partido
import com.example.applicacion.model.EstadisticaJugador
import retrofit2.http.*

interface ApiService {

    // ========== EQUIPOS ==========
    @POST("api/Equipo")
    suspend fun crearEquipo(
        @Body request: Equipo
    ): Equipo

    @GET("api/Equipo")
    suspend fun getEquipos(): List<Equipo>

    @GET("api/Equipo/{id}")
    suspend fun getEquipoPorId(
        @Path("id") id: Long
    ): Equipo

    @PUT("api/Equipo/{id}")
    suspend fun actualizarEquipo(
        @Path("id") id: Long,
        @Body request: Equipo
    ): Equipo

    @DELETE("api/Equipo/{id}")
    suspend fun eliminarEquipo(
        @Path("id") id: Long
    )

    // ========== JUGADORES ==========
    @POST("api/Jugador")
    suspend fun crearJugador(
        @Body request: Jugador
    ): Jugador

    @GET("api/Jugador")
    suspend fun getTodosJugadores(): List<Jugador>

    @GET("api/Jugador/{id}")
    suspend fun getJugadorPorId(
        @Path("id") id: Long
    ): Jugador

    @GET("api/Jugador/goles/{goles}")
    suspend fun jugadoresConMasGoles(
        @Path("goles") goles: Int
    ): List<Jugador>

    @GET("api/Jugador/equipo/{id}")
    suspend fun getJugadoresPorEquipo(
        @Path("id") idEquipo: Long
    ): List<Jugador>

    @PUT("api/Jugador/{id}")
    suspend fun actualizarJugador(
        @Path("id") id: Long,
        @Body request: Jugador
    ): Jugador

    @DELETE("api/Jugador/{id}")
    suspend fun eliminarJugador(
        @Path("id") id: Long
    )

    // ========== ENTRENADORES ==========
    @POST("api/Entrenador")
    suspend fun crearEntrenador(
        @Body request: Entrenador
    ): Entrenador

    @GET("api/Entrenador")
    suspend fun getEntrenadores(): List<Entrenador>

    @GET("api/Entrenador/{id}")
    suspend fun getEntrenadorPorId(
        @Path("id") id: Long
    ): Entrenador

    @PUT("api/Entrenador/{id}")
    suspend fun actualizarEntrenador(
        @Path("id") id: Long,
        @Body request: Entrenador
    ): Entrenador

    @DELETE("api/Entrenador/{id}")
    suspend fun eliminarEntrenador(
        @Path("id") id: Long
    )

    // ========== PARTIDOS ==========
    @POST("api/Partido")
    suspend fun crearPartido(
        @Body request: Partido
    ): Partido

    @GET("api/Partido")
    suspend fun getPartidos(): List<Partido>

    @GET("api/Partido/{id}")
    suspend fun getPartidoPorId(
        @Path("id") id: Long
    ): Partido

    @GET("api/Partido/equipos/{id}/goles")
    suspend fun getTotalGolesEquipo(
        @Path("id") idEquipo: Long
    ): Int

    @GET("api/Partido/resultados")
    suspend fun getResultados(): List<Any>

    @PUT("api/Partido/{id}")
    suspend fun actualizarPartido(
        @Path("id") id: Long,
        @Body request: Partido
    ): Partido

    @DELETE("api/Partido/{id}")
    suspend fun eliminarPartido(
        @Path("id") id: Long
    )

    // ========== ESTADÍSTICAS ==========
    @POST("api/Estadisticas")
    suspend fun crearEstadisticas(
        @Body request: EstadisticaJugador
    ): EstadisticaJugador

    @GET("api/Estadisticas/{id}")
    suspend fun getEstadisticaPorId(
        @Path("id") id: Long
    ): EstadisticaJugador

    @GET("api/Estadisticas/jugador/{id}")
    suspend fun getEstadisticasPorJugador(
        @Path("id") idJugador: Long
    ): List<EstadisticaJugador>

    @PUT("api/Estadisticas/{id}")
    suspend fun actualizarEstadisticas(
        @Path("id") id: Long,
        @Body request: EstadisticaJugador
    ): EstadisticaJugador

    @DELETE("api/Estadisticas/{id}")
    suspend fun eliminarEstadisticas(
        @Path("id") id: Long
    )
}