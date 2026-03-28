package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase

/**
 * Clase que gestiona el subrepositorio de estadisticas
 */
class EstadisticasRepository(database: TusTareasDatabase) {
    private val estadisticasConsultas = database.estadisticasConsultas()

    // Tareas completadas por dia
    fun tareasCompletadasPorDia(i: Long) = estadisticasConsultas.tareasCompletadasPorDia(i)
    // Tareas no completadas por dia
    fun tareasNoCompletadasPorDia(i: Long) = estadisticasConsultas.tareasNoCompletadasPorDia(i)
    // Obtener todas las tareas completadas
    fun obtenerCantidadTareasCompletas() = estadisticasConsultas.obtenerCantidadTareasCompletas()
    // Obtener todas las tareas pendientes
    fun obtenerCantidadTareasPendientes() = estadisticasConsultas.obtenerCantidadTareasPendientes()
    // Obtener todas las tareas retrasadas
    fun obtenerCantidadTareasRetrasadas() = estadisticasConsultas.obtenerCantidadTareasRetrasadas()
    // Obtener tareas completas entre 2 fechas
    fun obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio: Long, fechaFin: Long) = estadisticasConsultas.obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio, fechaFin)

    // Obtener tareas pendientes entre 2 fechas
    fun obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio: Long, fechaFin: Long) = estadisticasConsultas.obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio, fechaFin)
}