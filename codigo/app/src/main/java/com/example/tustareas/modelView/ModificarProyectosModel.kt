package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.ModificarProyectosRepository
import com.example.tustareas.repository.TusTareasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Clase que gestiona el submodelo de modificar proyecto
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de modificar proyectos
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class ModificarProyectosModel @Inject constructor(
    application: Application,
    private val repository: ModificarProyectosRepository
) : AndroidViewModel(application) {
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
        repository.obtenerTareasRestantes(texto, idProyecto)
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
        repository.obtenerEtiquetasRestantes(texto)
    }

    /**
     * Inserta un proyecto con sus tareas e etiquetas en la base de datos
     *
     * @param proyectoDTO El proyecto con sus tareas e etiquetas a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.insertarProyectoConTareaYEtiqueta(proyectoDTO)

    /**
     * Modifica un proyecto con sus tareas e etiquetas en la base de datos
     *
     * @param proyectoDTO El proyecto con sus tareas e etiquetas a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.modificarProyectoConTareaYEtiqueta(proyectoDTO)


}