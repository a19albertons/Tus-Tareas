package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import kotlin.collections.List

/**
 * Clase que gestiona el submodelo de modificar proyecto
 */
class ModificarProyectosModel(private val repository: TusTareasRepository) {
    // Obtener tareas restantes
    // Tiene la lista inicial (vacia)
    private val listaTareas = MutableLiveData<List<Tarea>>(emptyList<Tarea>())
    // Actualiza el filtro de la lista
    fun actualizarFiltroListaTareaProyecto(lista: List<Tarea>) {
        listaTareas.value = lista
    }
    // Obtiene las tareas restantes (libres)
    fun obtenerTareasRestantes() : LiveData<List<Tarea>> = listaTareas.switchMap {
            texto ->
        repository.modificarProyectos.obtenerTareasRestantes(texto)
    }

    // Tiene la lista inicial vacia
    private val listaEtiqueta = MutableLiveData<List<Etiqueta>>(emptyList<Etiqueta>())
    // Actualiza el filtro de la lista
    fun actualizarFiltroListaEtiquetaTareas(lista: List<Etiqueta>) {
        listaEtiqueta.value = lista
    }
    // Obtiene las etiquetas restantes que no tiene en uso
    fun obtenerEtiquetasRestantes() : LiveData<List<Etiqueta>> = listaEtiqueta.switchMap {
            texto ->
        repository.modificarProyectos.obtenerEtiquetasRestantes(texto)
    }

    // Metodo que inserta un proyecto con sus tarea e etiquetas
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)

    // Metodo que modifica un proyecto con sus tarea e etiquetas

    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyectoDTO)


}