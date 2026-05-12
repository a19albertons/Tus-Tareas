package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tustareas.R
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.ModificarEtiquetasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase que representa al submodelo de modificar etiqueta.
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de modificar etiquetas
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class ModificarEtiquetasModel @Inject constructor(
    application: Application,
    private val repository: ModificarEtiquetasRepository
) : AndroidViewModel(application) {

    private val _etiquetaModel : MutableLiveData<Etiqueta> = MutableLiveData()
    private val etiquetaModel : MutableLiveData<Etiqueta>
        get() = _etiquetaModel

    private val mensajeError : MutableLiveData<Int> = MutableLiveData()

    private val resultado : MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Observa el mensaje de error de la operación de guardar o modificar una etiqueta
     *
     * @return Un MutableLiveData que contiene un entero que representa el recurso del mensaje de error a mostrar en caso de que la operación de guardar o modificar una etiqueta falle
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun observarMensajeError() : MutableLiveData<Int> {
        return mensajeError
    }

    /**
     * Define la etiqueta a modificar
     *
     * @param etiqueta La etiqueta a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun definirEtiqueta(etiqueta: Etiqueta) {
        _etiquetaModel.value = etiqueta
    }

    /**
     * Observa la etiqueta a modificar
     *
     * @return Un MutableLiveData que contiene la etiqueta a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun observarEtiqueta() : MutableLiveData<Etiqueta> {
        return etiquetaModel
    }

    /**
     * Comprueba si la etiqueta a modificar es nueva o no
     *
     * @return true si la etiqueta a modificar es nueva (id == 0), false en caso contrario
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun esNuevaEtiqueta() : Boolean {
        return etiquetaModel.value!!.id == 0
    }



    /**
     * Guarda o modifica la etiqueta a modificar en la base de datos dependiendo de si es nueva o no
     *
     * @param nombre El nuevo nombre de la etiqueta a modificar
     * @param descripcion La nueva descripción de la etiqueta a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun guardarYModificarEtiqueta(nombre: String, descripcion: String) {
        // Comprobación de que el título de la etiqueta no está vacío
        if (nombre.isBlank()) {
            if (esNuevaEtiqueta()) {
                mensajeError.value = R.string.error_guardar_etiqueta
            }
            else {
                mensajeError.value = R.string.error_modificar_etiqueta
            }
            return
        }

        // Pasamos el filtro de nulos del if y actualizamos la clase etiqueta con los datos del formulario
        etiquetaModel.value!!.nombre = nombre
        etiquetaModel.value!!.descripcion = descripcion

        // Generamos un hilo donde se ejecuta la inserción
        viewModelScope.launch {
            try {
                if (esNuevaEtiqueta()) {
                    // Si es una nueva etiqueta, la insertamos
                    insertarEtiqueta(etiquetaModel.value!!)
                }
                else {
                    // Si no es una nueva etiqueta, la modificamos
                    modificarEtiqueta(etiquetaModel.value!!)
                }
                // Volvemos a la vista previa
                resultado.value = true
            }
            catch (_: Exception) {
                mensajeError.value = R.string.error_guardar_etiqueta

            }
        }
    }

    /**
     * Observa el resultado de la operación de guardar o modificar una etiqueta
     *
     * @return Un MutableLiveData que contiene un booleano que indica si la operación de guardar o
     * modificar una etiqueta se ha realizado correctamente (true) o no (false)
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun observarResultado() : MutableLiveData<Boolean> {
        return resultado
    }

    /**
     * Obtiene el título del diálogo de confirmación de guardado o modificación de una etiqueta
     * dependiendo de si la etiqueta a modificar es nueva o no
     *
     * @return Un entero que representa el recurso del título del diálogo de confirmación de guardado
     * o modificación de una etiqueta dependiendo de si la etiqueta a modificar es nueva o no
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun tituloDialogo(): Int {
        return if (esNuevaEtiqueta()) {
            R.string.confirmar_guardar_etiqueta
        }
        else {
            R.string.confirmar_modificar_etiqueta
        }
    }

    /**
     * Inserta una nueva etiqueta en la base de datos
     *
     * @param etiqueta La etiqueta a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.insertarEtiqueta(etiqueta)

    /**
     * Modifica una etiqueta en la base de datos
     *
     * @param etiqueta La etiqueta a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificarEtiqueta(etiqueta)
}