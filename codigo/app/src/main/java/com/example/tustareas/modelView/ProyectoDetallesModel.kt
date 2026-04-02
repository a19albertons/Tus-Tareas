package com.example.tustareas.modelView

import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de proyecto detalles
 */
class ProyectoDetallesModel(private val repository: TusTareasRepository) {
    // Obtieen un proyecto dto por id
    fun obtenerProyectoPorId(id: Int) = repository.proyectoDetalles.obtenerProyectoPorId(id)

    // Elimina un proyecto y sus relaciones
    suspend fun eliminarProyectoConTareaYEtiqueta(proyectoVisualizado: ProyectoDTO) = repository.proyectoDetalles.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado)

}