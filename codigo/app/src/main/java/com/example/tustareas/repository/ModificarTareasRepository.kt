package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que gestiona el subrepositorio de modificar tareas
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ModificarTareasRepository(database: TusTareasDatabase) {
    private val modificarTareaConsultas = database.modificarTareaConsultas()

    /**
     * Obtiene las etiquetas restantes de una tarea que no estan usadas en la tarea
     *
     * @param listaEtiquetas La lista de etiquetas que ya estan usadas en la tarea
     * @return Las etiquetas restantes de una tarea que no estan usadas en la tarea
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) = modificarTareaConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })

    /**
     * Inserta una nueva tarea en la base de datos con sus etiquetas
     *
     * @param tareaDTO El DTO de la tarea a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = modificarTareaConsultas.insertarTareaConEtiqueta(tareaDTO)

    /**
     * Modifica una tarea existente en la base de datos con sus etiquetas
     *
     * @param tareaDTO El DTO de la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) = modificarTareaConsultas.modificarTareaConEtiqueta(tareaDTO)
}