package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO

/**
 * Clase que gestiona el subrepositorio de proyecto detalles
 */
class ProyectoDetallesRepository(database: TusTareasDatabase) {
    private val proyectoDetallesConsultas = database.proyectoDetallesConsultas()
    // Obtener proyecto por id
    fun obtenerProyectoPorId(id: Int) = proyectoDetallesConsultas.obtenerProyectoPorId(id)

    // Eliminar proyecto
    suspend fun eliminarProyectoConTareaYEtiqueta(proyectoVisualizado: ProyectoDTO) = proyectoDetallesConsultas.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado.proyecto)
}