package com.example.tustareas.modelView

import android.widget.CheckBox
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
import com.google.android.material.snackbar.Snackbar
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
    fun actualizarPrioridadListadoTareas(prioridad: Array<Prioridad>) {
        prioridadTarea.value = prioridad
    }
    // Valor inicial de estado
    private val estadoTarea = MutableLiveData(Estado.entries.toTypedArray())
    fun actualizarEstadoListadoTareas(estado: Array<Estado>) {
        estadoTarea.value = estado
    }
    // Valor incial del filtro de la tarea
    private val textoTarea = MutableLiveData("")
    fun actualizarTextoListadoTareas(texto: String) {
        textoTarea.value = texto
    }
    // Valor inicial del filtro de ordenación del popup
    private val textoOrdenación = MutableLiveData(OrdenarTareas.FECHA_CREACION_ASC)
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

    // Si hay cambios en los filtros actualiza el resultado consultado en la base de datos
    fun obtenerTareasFiltradas() : LiveData<List<Tarea>> = vigiladorFiltrosTareas.switchMap {
        repository.listarTareas.obtenerTareasFiltradas(
            prioridadTarea.value!!,
            estadoTarea.value!!,
            textoTarea.value!!,
            textoOrdenación.value!!
        )

    }

    // Modificar tarea del adapter
    private suspend fun modificarTarea(tarea: Tarea) = repository.listarTareas.modificarTarea(tarea)

    // Gestiona el click en el checkbox de la tarea. Se mueve la lógica de negocio al view Model
    fun clickCheckbox(objectoActual: Tarea, checkBox: CheckBox) {
        if (checkBox.isChecked) {
            objectoActual.estado = Estado.Completada
            scope.launch {
                try {
                    modificarTarea(objectoActual)
                }
                catch (_: Exception) {
                    Snackbar.make(checkBox,checkBox.context.getString(R.string.error_modificar_checkbox), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        else {
            // Control de la fecha EnTiempo o Retrasada
            if (objectoActual.fechaLimite == null || objectoActual.fechaLimite!! >= DateHelper.fechaMediaNocheUTC()) {
                objectoActual.estado = Estado.EnTiempo
            }
            else {
                objectoActual.estado = Estado.Retrasada
            }
            scope.launch {
                try {
                    modificarTarea(objectoActual)
                }
                catch (_: Exception) {
                    Snackbar.make(checkBox,checkBox.context.getString(R.string.error_modificar_checkbox), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}