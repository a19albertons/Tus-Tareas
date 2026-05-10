package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import javax.inject.Inject

/**
 * Clase que representa al subrepositorio de modificar proyectos
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ModificarProyectosRepository @Inject constructor(
    private val database: TusTareasDatabase
) {
    private val modificarProyectoConsultas = database.modificarProyectoConsultas()

    /**
     * Obtiene las tareas restantes de un proyecto que no esten usadas por otros proyectos  y estan deseleccionada por el actual
     *
     * @param listaTareas La lista de tareas a comparar
     * @param idProyecto El ID del proyecto a comparar
     * @return Las tareas restantes de un proyecto que no esten usadas por otros proyectos y estan deseleccionada por el actual
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasRestantes(listaTareas: List<Tarea>, idProyecto: Int) = modificarProyectoConsultas.obtenerTareasRestantes(listaTareas.map { it.id }, idProyecto)

    /**
     * Obtiene las etiquetas restantes de un proyecto que no esten usadas
     *
     * @param listaEtiquetas La lista de etiquetas a comparar
     * @return Las etiquetas restantes de un proyecto que no esten usadas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) = modificarProyectoConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })

    /**
     * Inserta un proyecto nuevo con sus tareas y etiquetas
     *
     * @param proyectoDTO El DTO del proyecto a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = modificarProyectoConsultas.insertarProyectoConTareaYEtiqueta(proyectoDTO)

    /**
     * Modifica un proyecto existente con sus tareas y etiquetas
     *
     * @param proyectoDTO El DTO del proyecto a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = modificarProyectoConsultas.modificarProyectoConTareaYEtiqueta(proyectoDTO)
}