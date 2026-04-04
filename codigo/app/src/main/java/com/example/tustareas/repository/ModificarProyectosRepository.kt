package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea

/**
 * Clase que representa al subrepositorio de modificar proyectos
 */
class ModificarProyectosRepository(database: TusTareasDatabase) {
    private val modificarProyectoConsultas = database.modificarProyectoConsultas()

    // Obtener tareas restantes
    fun obtenerTareasRestantes(listaTareas: List<Tarea>) = modificarProyectoConsultas.obtenerTareasRestantes(listaTareas.map { it.id })

    // Obtener etiquetas restantes
    fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) = modificarProyectoConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })

    // Inserta un proyecto nuevo con sus tareas y etiquetas
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = modificarProyectoConsultas.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    // Modificar proyecto existente
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = modificarProyectoConsultas.modificarProyectoConTareaYEtiqueta(proyectoDTO)
}