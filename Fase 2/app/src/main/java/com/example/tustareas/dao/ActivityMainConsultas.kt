package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Estado

/**
 * Clase que representa las consultas de la actividad principal
 */
@Dao
interface ActivityMainConsultas {
    // Elimina las tareas completadas
    @Query("DELETE FROM tareas WHERE estado = :estado")
    suspend fun limpiarTareasCompletas(estado: Estado = Estado.Completada)
}