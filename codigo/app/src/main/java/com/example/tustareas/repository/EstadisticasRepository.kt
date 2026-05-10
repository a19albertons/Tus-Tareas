package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import javax.inject.Inject

/**
 * Clase que gestiona el subrepositorio de estadisticas
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class EstadisticasRepository @Inject constructor(
    database: TusTareasDatabase
) {
    private val estadisticasConsultas = database.estadisticasConsultas()

    /**
     * Obtiene la cantidad de tareas completadas por dia
     *
     * @param i El dia a consultar
     * @return La cantidad de tareas completadas por dia
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun tareasCompletadasPorDia(i: Long) = estadisticasConsultas.tareasCompletadasPorDia(i)

    /**
     * Obtiene la cantidad de tareas no completadas por dia
     *
     * @param i El dia a consultar
     * @return La cantidad de tareas no completadas por dia
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun tareasNoCompletadasPorDia(i: Long) = estadisticasConsultas.tareasNoCompletadasPorDia(i)

    /**
     * Obtiene la cantidad de tareas completadas
     *
     * @return La cantidad de tareas completadas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerCantidadTareasCompletas() = estadisticasConsultas.obtenerCantidadTareasCompletas()

    /**
     * Obtiene la cantidad de tareas pendientes
     *
     * @return La cantidad de tareas pendientes
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerCantidadTareasPendientes() = estadisticasConsultas.obtenerCantidadTareasPendientes()

    /**
     * Obtiene la cantidad de tareas retrasadas
     *
     * @return La cantidad de tareas retrasadas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerCantidadTareasRetrasadas() = estadisticasConsultas.obtenerCantidadTareasRetrasadas()

    /**
     * Obtiene la cantidad de tareas completas entre 2 fechas
     *
     * @param fechaInicio La fecha de inicio
     * @param fechaFin La fecha de fin
     * @return La cantidad de tareas completas entre 2 fechas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio: Long, fechaFin: Long) = estadisticasConsultas.obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio, fechaFin)

    /**
     * Obtiene la cantidad de tareas pendientes entre 2 fechas
     *
     * @param fechaInicio La fecha de inicio
     * @param fechaFin La fecha de fin
     * @return La cantidad de tareas pendientes entre 2 fechas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio: Long, fechaFin: Long) = estadisticasConsultas.obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio, fechaFin)
}