package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de modificar tareas
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ModificarTareasModel(private val repository: TusTareasRepository) {
    // Filtro etiquetas modificar
    private val listaEtiqueta = MutableLiveData<List<Etiqueta>>(emptyList())

    /**
     * Actualiza el filtro de la lista de etiquetas de la tarea
     *
     * @param lista La nueva lista de etiquetas de la tarea
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarFiltroListaEtiquetaTareas(lista: List<Etiqueta>) {
        listaEtiqueta.value = lista
    }

    /**
     * Obtiene las etiquetas restantes (libres) que no tiene la tarea
     *
     * @return Un LiveData que contiene una lista de etiquetas restantes (libres) que no tiene la tarea
     * @author Alberto Noceda <a19albertons@iessanclement
     */
    fun obtenerEtiquetasRestantes() : LiveData<List<Etiqueta>> = listaEtiqueta.switchMap {
            texto ->
        repository.modificarTareas.obtenerEtiquetasRestantes(texto)
    }

    /**
     * Inserta una nueva tarea con sus etiquetas en la base de datos
     *
     * @param tareaDTO La tarea a insertar con sus etiquetas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.modificarTareas.insertarTareaConEtiqueta(tareaDTO)

    /**
     * Modifica una tarea con sus etiquetas en la base de datos
     *
     * @param tareaDTO La tarea a modificar con sus etiquetas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.modificarTareas.modificarTareaConEtiqueta(tareaDTO)

}