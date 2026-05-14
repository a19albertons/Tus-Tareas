package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.tustareas.R
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.repository.ModificarTareasRepository
import com.example.tustareas.util.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * Clase que gestiona el submodelo de modificar tareas
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de modificar tareas
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class ModificarTareasModel @Inject constructor(
    application: Application,
    private val repository: ModificarTareasRepository
) : AndroidViewModel(application) {
    // Filtro etiquetas modificar
    private val listaEtiqueta = MutableLiveData<List<Etiqueta>>(emptyList())

    // Variables de tareasDTO
    private val _tareaDTO : MutableLiveData<TareaDTO> = MutableLiveData()
    private val tareaDTO : MutableLiveData<TareaDTO>
        get() = _tareaDTO

    private val mensajeError : MutableLiveData<Int> = MutableLiveData()

    private val resultado : MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Define la tarea a modificar
     *
     * @param tareaDTO La tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun definirTareaDTO(tareaDTO: TareaDTO) {
        _tareaDTO.value = tareaDTO
    }

    /**
     * Observa la tarea a modificar
     *
     * @return Un MutableLiveData que contiene la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun observarTareaDTO() : MutableLiveData<TareaDTO> {
        return tareaDTO
    }

    /**
     * Obtiene la prioridad de la tarea a modificar
     *
     * @return Un entero que representa la prioridad de la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun prioridadOrdinal() : Int {
        return tareaDTO.value!!.tarea.prioridad.ordinal
    }

    /**
     * Cambia la prioridad de la tarea a modificar
     *
     * @param prioridad Un entero que representa la nueva prioridad de la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun cambiarPrioridad(prioridad: Int) {
        when (prioridad) {
            0 -> _tareaDTO.value!!.tarea.prioridad = Prioridad.ALTA
            1 -> _tareaDTO.value!!.tarea.prioridad = Prioridad.MEDIA
            2 -> _tareaDTO.value!!.tarea.prioridad = Prioridad.BAJA
            3 -> _tareaDTO.value!!.tarea.prioridad = Prioridad.NO_ESTABLECIDO
            else -> _tareaDTO.value!!.tarea.prioridad = Prioridad.ALTA
        }
    }

    /**
     * Actualiza la fecha límite de la tarea a modificar
     *
     * @param fechaLimite La nueva fecha límite de la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarFechaLimite(fechaLimite: Date) {
        _tareaDTO.value!!.tarea.fechaLimite = fechaLimite
    }

    /**
     * Comprueba la lista de etiquetas y devuelve una etiqueta indicando que no hay etiquetas si la lista está vacía
     *
     * @param etiquetas La lista de etiquetas a comprobar
     * @return Una lista de etiquetas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun comprobarListaEtiquetas(etiquetas: List<Etiqueta>) : List<Etiqueta> {
        // Comprueba si quedan etiquetas, si no quedan, devuelve una lista con una etiqueta que indica que no hay etiquetas
        return etiquetas.ifEmpty {
            listOf(
                Etiqueta(
                    0,
                    getApplication<Application>().getString(R.string.no_existen_etiquetas)
                )
            )
        }
    }

    /**
     * Actualiza las etiquetas de la tarea a modificar
     *
     * @param lista La nueva lista de etiquetas de la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarEtiquetasTarea(lista: List<Etiqueta>) {
        _tareaDTO.value!!.etiquetas = lista
    }

    /**
     * Obtiene la lista de etiquetas de la tarea a modificar
     *
     * @return Una lista de etiquetas de la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerListaEtiquetasTarea() : List<Etiqueta> {
        return tareaDTO.value!!.etiquetas
    }

    /**
     * Obtiene el título del diálogo de confirmación dependiendo de si la tarea a modificar es nueva o no
     *
     * @return Un entero que representa el recurso del título del diálogo de confirmación a mostrar dependiendo de si la tarea a modificar es nueva o no
     * @author Alberto Noceda <a19albertons@iessanclemente.net
     */
    fun tituloDialogo() : Int {
        return if (tareaDTO.value!!.tarea.id == 0) {
            R.string.confirmar_guardar_tarea
        }
        else {
            R.string.confirmar_modificado_tarea
        }
    }

    /**
     * Guarda o modifica la tarea a modificar en la base de datos dependiendo de si es nueva o no
     *
     * @param nombre El nuevo nombre de la tarea a modificar
     * @param descripcion La nueva descripción de la tarea a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun guardarYModificarTarea(nombre: String, descripcion: String) {
        // Comprobación de que el título de la tarea no está vacío
        if (nombre.isBlank()) {
            if (tareaDTO.value!!.tarea.id == 0) {
                mensajeError.value = R.string.error_guardar_tarea
            }
            else {
                mensajeError.value = R.string.error_modificar_tarea
            }
            return
        }

        // Actualizamos los campos de texto con los ultimo
        _tareaDTO.value!!.tarea.nombre = nombre
        _tareaDTO.value!!.tarea.descripcion = descripcion

        // Control logica tarea completa o no en función de la fecha limite y la fecha actual
        if (tareaDTO.value!!.tarea.estado != Estado.COMPLETADA) {
            if (tareaDTO.value!!.tarea.fechaLimite == null || tareaDTO.value!!.tarea.fechaLimite!!.after(DateHelper.fechaMediaNocheUTC())) {
                _tareaDTO.value!!.tarea.estado = Estado.EN_TIEMPO
            }
            else {
                tareaDTO.value!!.tarea.estado = Estado.RETRASADA
            }
        }

        // Generamos un hilo con la nueva tarea
        viewModelScope.launch {
            try {
                if (tareaDTO.value!!.tarea.id == 0) {
                    insertarTareaConEtiqueta(tareaDTO.value!!)
                }
                else {
                    modificarTareaConEtiqueta(tareaDTO.value!!)
                }
                // Volvemos a la vista previa
                resultado.value = true
            }
            catch (_: Exception) {
                mensajeError.value = R.string.error_guardar_tarea
            }
        }
    }

    /**
     * Observa el mensaje de error
     *
     * @return Un MutableLiveData que contiene el mensaje de error a mostrar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun observarMensajeError() : MutableLiveData<Int> {
        return mensajeError
    }

    /**
     * Observa el resultado de la operación de guardar o modificar la tarea
     *
     * @return Un MutableLiveData que contiene un booleano que indica si la operación de guardar o modificar la tarea ha sido exitosa
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun observarResultado() : MutableLiveData<Boolean> {
        return resultado
    }
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
        repository.obtenerEtiquetasRestantes(texto)
    }

    /**
     * Inserta una nueva tarea con sus etiquetas en la base de datos
     *
     * @param tareaDTO La tarea a insertar con sus etiquetas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.insertarTareaConEtiqueta(tareaDTO)

    /**
     * Modifica una tarea con sus etiquetas en la base de datos
     *
     * @param tareaDTO La tarea a modificar con sus etiquetas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.modificarTareaConEtiqueta(tareaDTO)

}