package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Tarea

/**
 * Clase que gestiona el subrepositorio de tarea detalles
 */
class TareaDetallesRepository(database: TusTareasDatabase) {
    private val tareaDetallesConsulta = database.tareaDetallesConsulta()
    // Obtener tarea dto por id
    fun obtenerTareaDTOPorID(id: Int) = tareaDetallesConsulta.obtenerTareaDTOPorID(id)
    // Borrar tarea
    suspend fun eliminarTarea(tarea: Tarea) = tareaDetallesConsulta.eliminarTarea(tarea)
}