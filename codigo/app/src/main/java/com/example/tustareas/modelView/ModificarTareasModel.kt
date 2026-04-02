package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de modificar tareas
 */
class ModificarTareasModel(private val repository: TusTareasRepository) {
    // Filtro etiquetas modificar
    private val listaEtiqueta = MutableLiveData<List<Etiqueta>>(emptyList<Etiqueta>())
    fun actualizarFiltroListaEtiquetaTareas(lista: List<Etiqueta>) {
        listaEtiqueta.value = lista
    }
    fun obtenerEtiquetasRestantes() : LiveData<List<Etiqueta>> = listaEtiqueta.switchMap {
            texto ->
        repository.modificarTareas.obtenerEtiquetasRestantes(texto)
    }
    // Insertar una nueva tarea con sus etiquetas
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.modificarTareas.insertarTareaConEtiqueta(tareaDTO)
    // Modificación de una tarea y sus etiquetas
    suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.modificarTareas.modificarTareaConEtiqueta(tareaDTO)

}