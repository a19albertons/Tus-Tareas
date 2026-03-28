package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase

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
}