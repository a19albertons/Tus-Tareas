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
    // Hace una consulta a las tareas devolviendo aquellas que estean compleas
    @Query("select count(*) from tareas where fechaLimite = :i and estado = :estado")
    fun tareasCompletadasPorDia(i: Long, estado: Estado = Estado.Completada): LiveData<Float>

    // Hace una consulta a las tareas devolviendo las no completas
    @Query("select count(*) from tareas where fechaLimite = :i and estado != :estado")
    fun tareasNoCompletadasPorDia(i: Long, estado: Estado = Estado.Completada): LiveData<Float>

    // Hace una consulta a las tareas devolviendo las completas
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasCompletas(estado: Estado = Estado.Completada): LiveData<Int>

    // Hace una consulta a las tareas devolviendo las que estan en tiempo
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasPendientes(estado: Estado = Estado.EnTiempo): LiveData<Int>

    // Hace una consulta a las tareas devolviendo las que estan retrasadas
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasRetrasadas(estado: Estado = Estado.Retrasada): LiveData<Int>

    // Hace una consulta buscando aquellas que estean entre dos fechas y completas
    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio: Long, fechaFin: Long, estado: Estado = Estado.Completada): LiveData<Long>

    // Hace una consulta buscando aquellas que estean entre dos fechas y en tiempo
    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio: Long, fechaFin: Long, estado: Estado = Estado.EnTiempo): LiveData<Long>
}