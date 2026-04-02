package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Tarea

/**
 * Clase que gestiona el subrepositorio del fragmento ver mas
 */
class VerMasRepository(database: TusTareasDatabase) {
    private val verMasConsultas = database.verMasConsultas()
    // Obtener tareas terminan dia especifico con filtro
    fun obtenerTareasTerminanDiaEspecificoConFiltro(texto: String) = verMasConsultas.obtenerTareasTerminanDiaEspecificoConFiltro(texto)
    // Obtener tareas retrasadas con filtro
    fun obtenerTareasRetrasadasConFiltro(texto: String) = verMasConsultas.obtenerTareasRetrasadasConFiltro(texto)
    // Obtener tareas proximas con filtro
    fun obtenerTareasProximasConFiltro(texto: String) = verMasConsultas.obtenerTareasProximasConFiltro(texto)

    // Modificar tarea existente para el adapter
    suspend fun modificarTarea(tarea: Tarea) = verMasConsultas.modificarTarea(tarea)
}