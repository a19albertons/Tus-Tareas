package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que gestiona el subrepositorio de modificar tareas
 */
class ModificarTareasRepository(database: TusTareasDatabase) {
    private val modificarTareaConsultas = database.modificarTareaConsultas()
    // Obtener etiquetas restantes
    fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) = modificarTareaConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })

    // Insertar nueva tarea
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = modificarTareaConsultas.insertarTareaConEtiqueta(tareaDTO)
    // Modifocar tarea con etiqueta
    suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) = modificarTareaConsultas.modificarTareaConEtiqueta(tareaDTO)
}