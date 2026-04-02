package com.example.tustareas.modelView

import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de tarea detalles
 */
class TareaDetallesModel(private val repository: TusTareasRepository) {
    // Obtiene un tarea dto por id
    fun obtenerTareaDTOPorID(id: Int) = repository.tareaDetalles.obtenerTareaDTOPorID(id)
    // Borra una tarea
    suspend fun eliminarTarea(tarea: Tarea) = repository.tareaDetalles.eliminarTarea(tarea)

}