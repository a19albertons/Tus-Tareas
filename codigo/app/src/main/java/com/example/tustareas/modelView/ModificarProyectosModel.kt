package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de modificar proyecto
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ModificarProyectosModel(private val repository: TusTareasRepository) {
    // Obtener tareas restantes
    // Tiene la lista inicial (vacia)
    private val listaTareas = MutableLiveData<List<Tarea>>(emptyList<Tarea>())

    /**
     * Actualiza el filtro de la lista de tareas del proyecto
     *
     * @param lista La nueva lista de tareas del proyecto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarFiltroListaTareaProyecto(lista: List<Tarea>) {
        listaTareas.value = lista
    }

    /**
     * Obtiene las tareas restantes (libres) que no tiene el proyecto
     *
     * @param idProyecto El ID del proyecto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasRestantes(idProyecto: Int) : LiveData<List<Tarea>> = listaTareas.switchMap {
            texto ->
        repository.modificarProyectos.obtenerTareasRestantes(texto, idProyecto)
    }

    // Tiene la lista inicial vacia
    private val listaEtiqueta = MutableLiveData<List<Etiqueta>>(emptyList<Etiqueta>())

    /**
     * Actualiza el filtro de la lista de etiquetas del proyecto
     *
     * @param lista La nueva lista de etiquetas del proyecto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarFiltroListaEtiquetaProyecto(lista: List<Etiqueta>) {
        listaEtiqueta.value = lista
    }

    /**
     * Obtiene las etiquetas restantes (libres) que no tiene el proyecto actual ni otros
     *
     * @return Un LiveData que contiene una lista de etiquetas restantes (libres) que no tiene el proyecto actual ni otros
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerEtiquetasRestantes() : LiveData<List<Etiqueta>> = listaEtiqueta.switchMap {
            texto ->
        repository.modificarProyectos.obtenerEtiquetasRestantes(texto)
    }

    /**
     * Inserta un proyecto con sus tareas e etiquetas en la base de datos
     *
     * @param proyectoDTO El proyecto con sus tareas e etiquetas a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)

    /**
     * Modifica un proyecto con sus tareas e etiquetas en la base de datos
     *
     * @param proyectoDTO El proyecto con sus tareas e etiquetas a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyectoDTO)


}