package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import java.util.Date

/**
 * Clase creada especificamente para poseer todas las operaciones sobre bd de consultas en inicio fragment
 */
@Dao
interface InicioConsultas {
    // Obtener todas las tareas de un dia especifico en tiempo con relación a su fecha limite
    @Query("select * from tareas where fechaLimite = :fecha AND estado = :estado")
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date, estado: Estado = Estado.EnTiempo): LiveData<List<Tarea>>

    // Obtener todas las tareas retrasadas
    @Query("select * from tareas where estado = :estado")
    fun obtenerTareasRetrasadas(estado: Estado = Estado.Retrasada): LiveData<List<Tarea>>

    // Obtener todas las tareas futuras no finalizadas
    @Query("select * from tareas where (fechaLimite > :fecha OR fechaLimite is null) AND estado = :estado ORDER BY fechaLimite ASC")
    fun obtenerTareasProximas(fecha: Date, estado: Estado = Estado.EnTiempo): LiveData<List<Tarea>>
}