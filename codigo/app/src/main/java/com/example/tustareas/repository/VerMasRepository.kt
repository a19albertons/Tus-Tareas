package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Tarea

/**
 * Clase que gestiona el subrepositorio del fragmento ver mas
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class VerMasRepository(database: TusTareasDatabase) {
    private val verMasConsultas = database.verMasConsultas()

    /**
     * Obtiene las tareas que terminan en un día específico con un filtro de texto
     *
     * @param texto El texto a filtrar en las tareas
     * @return Las tareas que terminan en un día específico y coinciden con el filtro
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasTerminanDiaEspecificoConFiltro(texto: String) = verMasConsultas.obtenerTareasTerminanDiaEspecificoConFiltro(texto)

    /**
     * Obtiene las tareas retrasadas con un filtro de texto
     *
     * @param texto El texto a filtrar en las tareas
     * @return Las tareas retrasadas que coinciden con el filtro
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasRetrasadasConFiltro(texto: String) = verMasConsultas.obtenerTareasRetrasadasConFiltro(texto)

    /**
     * Obtiene las tareas próximas con un filtro de texto
     *
     * @param texto El texto a filtrar en las tareas
     * @return Las tareas próximas que coinciden con el filtro
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasProximasConFiltro(texto: String) = verMasConsultas.obtenerTareasProximasConFiltro(texto)

    /**
     * Modifica una tarea existente en la base de datos
     *
     * @param tarea La tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarTarea(tarea: Tarea) = verMasConsultas.modificarTarea(tarea)
}