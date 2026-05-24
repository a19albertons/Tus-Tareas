package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Estado

/**
 * Clase que gestiona las consultas de estadisticas contra la bd
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface EstadisticasConsultas {
    /**
     * Hace una consulta a las tareas devolviendo aquellas que estén completas.
     *
     * @param i El timestamp del día para el cual se desea obtener la cantidad de tareas completadas.
     * @param estado El estado de las tareas a contar, por defecto es Estado.Completada. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<Float> con la cantidad de tareas completadas para el día especificado.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select count(*) from tareas where fechaLimite = :i and estado = :estado")
    fun tareasCompletadasPorDia(
        i: Long,
        estado: Estado = Estado.COMPLETADA,
    ): LiveData<Float>

    /**
     * Hace una consulta a las tareas devolviendo aquellas que no estén completas.
     *
     * @param i El timestamp del día para el cual se desea obtener la cantidad de tareas no completadas.
     * @param estado El estado de las tareas a contar, por defecto es Estado.Completada. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<Float> con la cantidad de tareas no completadas para el día especificado.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select count(*) from tareas where fechaLimite = :i and estado != :estado")
    fun tareasNoCompletadasPorDia(
        i: Long,
        estado: Estado = Estado.COMPLETADA,
    ): LiveData<Float>

    /**
     * Hace una consulta a las tareas devolviendo aquellas que estén completas.
     *
     * @param estado El estado de las tareas a contar, por defecto es Estado.Completada. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<Int> con la cantidad de tareas completas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasCompletas(estado: Estado = Estado.COMPLETADA): LiveData<Int>

    /**
     * Hace una consulta a las tareas devolviendo aquellas que estén en tiempo.
     *
     * @param estado El estado de las tareas a contar, por defecto es Estado.EnTiempo. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<Int> con la cantidad de tareas en tiempo.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasPendientes(estado: Estado = Estado.EN_TIEMPO): LiveData<Int>

    /**
     * Hace una consulta a las tareas devolviendo aquellas que estén retrasadas.
     *
     * @param estado El estado de las tareas a contar, por defecto es Estado.Retrasada. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<Int> con la cantidad de tareas retrasadas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasRetrasadas(estado: Estado = Estado.RETRASADA): LiveData<Int>

    /**
     * Hace una consulta buscando aquellas que estean entre dos fechas y completas
     *
     * @param fechaInicio El timestamp del día de inicio para el cual se desea obtener la cantidad de tareas completadas.
     * @param fechaFin El timestamp del día de fin para el cual se desea obtener la cantidad de tareas completadas.
     * @param estado El estado de las tareas a contar, por defecto es Estado.Completada. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<Long> con la cantidad de tareas completadas entre las dos fechas especificadas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasCompletasEntre2Fechas(
        fechaInicio: Long,
        fechaFin: Long,
        estado: Estado = Estado.COMPLETADA,
    ): LiveData<Long>

    /**
     * Hace una consulta buscando aquellas que estean entre dos fechas y en tiempo
     *
     * @param fechaInicio El timestamp del día de inicio para el cual se desea obtener la cantidad de tareas en tiempo.
     * @param fechaFin El timestamp del día de fin para el cual se desea obtener la cantidad de tareas en tiempo.
     * @param estado El estado de las tareas a contar, por defecto es Estado.EnTiempo. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<Long> con la cantidad de tareas en tiempo entre las dos fechas especificadas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasPendientesEntre2Fechas(
        fechaInicio: Long,
        fechaFin: Long,
        estado: Estado = Estado.EN_TIEMPO,
    ): LiveData<Long>
}
