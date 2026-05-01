package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.R
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.util.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Clase que gestiona el submodelo del fragmento listar tareas
 */
class ListarTareasModel(
    private val repository: TusTareasRepository,
    private val scope: CoroutineScope
) {
    // Filtro para tareas
    // Valor inicial de prioridad
    private val prioridadTarea = MutableLiveData(Prioridad.entries.toTypedArray())

    /**
     * Actualiza el valor del filtro de prioridad para las tareas
     *
     * @param prioridad El nuevo valor del filtro de prioridad para las tareas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarPrioridadListadoTareas(prioridad: Array<Prioridad>) {
        prioridadTarea.value = prioridad
    }

    // Valor inicial de estado
    private val estadoTarea = MutableLiveData(Estado.entries.toTypedArray())

    /**
     * Actualiza el valor del filtro de estado para las tareas
     *
     * @param estado El nuevo valor del filtro de estado para las tareas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarEstadoListadoTareas(estado: Array<Estado>) {
        estadoTarea.value = estado
    }

    // Valor incial del filtro de la tarea
    private val textoTarea = MutableLiveData("")

    /**
     * Actualiza el valor del filtro de texto para las tareas
     *
     * @param texto El nuevo valor del filtro de texto para las tareas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarTextoListadoTareas(texto: String) {
        textoTarea.value = texto
    }

    // Valor inicial del filtro de ordenación del popup
    private val textoOrdenación = MutableLiveData(OrdenarTareas.FECHA_CREACION_ASC)

    /**
     * Actualiza el valor del filtro de ordenación para las tareas
     *
     * @param nuevaOrdenacion El nuevo valor del filtro de ordenación para las tareas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarTextoOrdenacionListadoTareas(nuevaOrdenacion: OrdenarTareas) {
        textoOrdenación.value = nuevaOrdenacion
    }


    // Necesario para 2 o más filtros sobre el mismo observer (query)
    // Cuidado con los parentesis son traicioneros. Vigila cualquier tipo de cambio en los filtros
    private val vigiladorFiltrosTareas = MediatorLiveData<Unit>().apply {
        addSource(prioridadTarea) { value = Unit }
        addSource(estadoTarea) { value = Unit }
        addSource(textoTarea) { value = Unit }
        addSource(textoOrdenación) { value = Unit }
    }

    /**
     * Obtiene la lista de tareas filtradas según los filtros de prioridad, estado, texto y ordenación
     *
     * @return Un LiveData que contiene una lista de tareas filtradas según los filtros de prioridad, estado, texto y ordenación
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasFiltradas() : LiveData<List<Tarea>> = vigiladorFiltrosTareas.switchMap {
        repository.listarTareas.obtenerTareasFiltradas(
            prioridadTarea.value!!,
            estadoTarea.value!!,
            textoTarea.value!!,
            textoOrdenación.value!!
        )

    }

    /**
     * Modifica una tarea en la base de datos.
     *
     * @param tarea La tarea que se desea modificar.
     * @return Un objeto Result que indica el éxito o fracaso de la operación de modificación.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private suspend fun modificarTarea(tarea: Tarea) = repository.listarTareas.modificarTarea(tarea)

    // Variable para gestionar errores desde el ViewModel
    val mensajeError = MutableLiveData<Int?>(null)

    /**
     * Actualiza el estado de una tarea según el valor del checkbox.
     *
     * @param objectoActual La tarea que se desea actualizar.
     * @param isChecked Actualiza el estaod en función del valor del isChecked
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarEstadoTarea(objectoActual: Tarea, isChecked: Boolean) {
        // Comprueba el checkbox y actualiza el estado de la tarea en consecuencia
        if (isChecked) {
            objectoActual.estado = Estado.COMPLETADA
            // Control de errores y ejecución de la modificación en segundo plano
            scope.launch {
                try {
                    modificarTarea(objectoActual)
                }
                catch (_: Exception) {
                    mensajeError.value = R.string.error_modificar_checkbox
                }
            }
        }
        else {
            // Control de la fecha EnTiempo o Retrasada
            if (objectoActual.fechaLimite == null || objectoActual.fechaLimite!! >= DateHelper.fechaMediaNocheUTC()) {
                objectoActual.estado = Estado.EN_TIEMPO
            }
            else {
                objectoActual.estado = Estado.RETRASADA
            }

            // Control de errores y ejecución de la modificación en segundo plano
            scope.launch {
                try {
                    modificarTarea(objectoActual)
                }
                catch (_: Exception) {
                    mensajeError.value = R.string.error_modificar_checkbox
                }
            }
        }
    }
}
