package com.example.tustareas.modelView

import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.R
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.util.DateHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Clase que gestiona el submodelo del fragmento ver mas
 */
class VerMasModel(
    private val repository: TusTareasRepository,
    private val scope: CoroutineScope
) {
    // Guarda la variable de texto del filtro
    private val textoVerMas = MutableLiveData("")

    // Actualiza el texto si hay actualización
    fun actualizarTextoVerMas(texto: String) {
        textoVerMas.value = texto
    }
    // Mandan la petición al repositorio correspondiente para obtener los datos de la bd
    fun obtenerTareasTerminanDiaEspecificoConFiltro() : LiveData<List<Tarea>> = textoVerMas.switchMap {
            texto ->
        repository.verMas.obtenerTareasTerminanDiaEspecificoConFiltro(texto)
    }
    fun obtenerTareasRetrasadasConFiltro() : LiveData<List<Tarea>> = textoVerMas.switchMap {
            texto ->
        repository.verMas.obtenerTareasRetrasadasConFiltro(texto)
    }
    fun obtenerTareasProximasConFiltro() : LiveData<List<Tarea>> = textoVerMas.switchMap {
            texto ->
        repository.verMas.obtenerTareasProximasConFiltro(texto)
    }

    // Modificar tarea del adapter
    private suspend fun modificarTarea(tarea: Tarea) = repository.verMas.modificarTarea(tarea)

    // Variable para gestionar errores desde el ViewModel
    val mensajeError = MutableLiveData<Int?>(null)

    // Gestiona el click en el checkbox de la tarea. Se mueve la lógica de negocio al view Model
    fun actualizarEstado(objectoActual: Tarea, booleano: Boolean) {
        if (booleano) {
            objectoActual.estado = Estado.Completada
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
                    mensajeError.value = R.string.error_modificar_checkbox
                }
            }
        }
    }
}