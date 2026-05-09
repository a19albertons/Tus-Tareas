package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.tustareas.R
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.repository.VerMasRepository
import com.example.tustareas.util.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase que gestiona el submodelo del fragmento ver mas
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class VerMasModel @Inject constructor(
    application: Application,
    private val repository: VerMasRepository
) : AndroidViewModel(application) {
    // Guarda la variable de texto del filtro
    private val textoVerMas = MutableLiveData("")

    /**
     * Actualiza el valor del filtro de texto para las tareas
     *
     * @param texto El nuevo valor del filtro de texto para las tareas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarTextoVerMas(texto: String) {
        textoVerMas.value = texto
    }

    /**
     * Obtiene las tareas que terminan en un día específico con el filtro de texto
     *
     * @return Un LiveData que contiene una lista de tareas que terminan en un día específico con el filtro de texto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasTerminanDiaEspecificoConFiltro() : LiveData<List<Tarea>> = textoVerMas.switchMap {
            texto ->
        repository.obtenerTareasTerminanDiaEspecificoConFiltro(texto)
    }

    /**
     * Obtiene las tareas retrasadas con el filtro de texto
     *
     * @return Un LiveData que contiene una lista de tareas retrasadas con el filtro de texto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasRetrasadasConFiltro() : LiveData<List<Tarea>> = textoVerMas.switchMap {
            texto ->
        repository.obtenerTareasRetrasadasConFiltro(texto)
    }

    /**
     * Obtiene las tareas próximas con el filtro de texto
     *
     * @return Un LiveData que contiene una lista de tareas próximas con el filtro de texto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasProximasConFiltro() : LiveData<List<Tarea>> = textoVerMas.switchMap {
            texto ->
        repository.obtenerTareasProximasConFiltro(texto)
    }

    /**
     * Modifica una tarea en la base de datos
     *
     * @param tarea La tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private suspend fun modificarTarea(tarea: Tarea) = repository.modificarTarea(tarea)

    // Variable para gestionar errores desde el ViewModel
    val mensajeError = MutableLiveData<Int?>(null)

    /**
     * Actualiza el estado de una tarea según el valor del checkbox.
     *
     * @param objectoActual La tarea que se desea actualizar.
     * @param booleano Actualiza el estaod en función del valor de booleano
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarEstado(objectoActual: Tarea, booleano: Boolean) {
        if (booleano) {
            objectoActual.estado = Estado.COMPLETADA

            // Control de errores y ejecución de la modificación en segundo plano
            viewModelScope.launch {
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

            // Control de errores y ejecución de la modificación en segundo plan
            viewModelScope.launch {
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