package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Estado
import java.util.Date

/**
 * Clase que representa las consultas contra la bd de las tareas programadas
 */
@Dao
interface WorkerConsultas {
    @Query("UPDATE tareas SET estado = :estadoNuevo where fechaLimite < :fecha AND estado = :estado")
    fun actualizarEstado(fecha: Date = Date(), estado: Estado = Estado.EnTiempo, estadoNuevo: Estado = Estado.Retrasada)
}