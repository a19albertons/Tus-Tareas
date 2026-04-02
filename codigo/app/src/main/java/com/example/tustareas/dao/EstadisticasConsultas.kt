package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Estado

/**
 * Clase que gestiona las consultas de estadisticas contra la bd
 */
@Dao
interface EstadisticasConsultas {
    @Query("select count(*) from tareas where fechaLimite = :i and estado = :estado")
    fun tareasCompletadasPorDia(i: Long, estado: Estado = Estado.Completada): LiveData<Float>

    @Query("select count(*) from tareas where fechaLimite = :i and estado != :estado")
    fun tareasNoCompletadasPorDia(i: Long, estado: Estado = Estado.Completada): LiveData<Float>

    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasCompletas(estado: Estado = Estado.Completada): LiveData<Int>
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasPendientes(estado: Estado = Estado.EnTiempo): LiveData<Int>
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasRetrasadas(estado: Estado = Estado.Retrasada): LiveData<Int>

    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio: Long, fechaFin: Long, estado: Estado = Estado.Completada): LiveData<Long>

    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio: Long, fechaFin: Long, estado: Estado = Estado.EnTiempo): LiveData<Long>
}