package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tustareas.R
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.CrearEtiquetasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase que representa al submodelo de crear etiqueta.
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de crear etiquetas
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class CrearEtiquetasModel
    @Inject
    constructor(
        application: Application,
        private val repository: CrearEtiquetasRepository,
    ) : AndroidViewModel(application) {
        private val _etiquetaModel: MutableLiveData<Etiqueta> = MutableLiveData()
        val etiquetaModel: MutableLiveData<Etiqueta>
            get() = _etiquetaModel

        private val mensajeError: MutableLiveData<Int> = MutableLiveData()

        private val resultado: MutableLiveData<Boolean> = MutableLiveData(false)

        /**
         * Observa el mensaje de error de la operación de guardar una etiqueta
         *
         * @return Un MutableLiveData que contiene un entero que representa el recurso del mensaje de error a mostrar en caso de que la operación de guardar una etiqueta falle
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun observarMensajeError(): MutableLiveData<Int> = mensajeError

        /**
         * Define la etiqueta a crear
         *
         * @param etiqueta La etiqueta a crear
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun definirEtiqueta(etiqueta: Etiqueta) {
            _etiquetaModel.value = etiqueta
        }

        /**
         * Observa la etiqueta a crear
         *
         * @return Un MutableLiveData que contiene la etiqueta a crear
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun observarEtiqueta(): MutableLiveData<Etiqueta> = etiquetaModel


        /**
         * Guarda la etiqueta a crear en la base de datos
         *
         * @param nombre El nuevo nombre de la etiqueta a guardar
         * @param descripcion La nueva descripción de la etiqueta a guardar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun guardarEtiqueta(
            nombre: String,
            descripcion: String,
        ) {
            // Comprobación de que el título de la etiqueta no está vacío
            if (nombre.isBlank()) {
                mensajeError.value = R.string.error_guardar_etiqueta
                return
            }

            // Pasamos el filtro de nulos del if y actualizamos la clase etiqueta con los datos del formulario
            etiquetaModel.value!!.nombre = nombre
            etiquetaModel.value!!.descripcion = descripcion

            // Generamos un hilo donde se ejecuta la inserción
            viewModelScope.launch {
                try {
                    // Si es una nueva etiqueta, la insertamos
                    insertarEtiqueta(etiquetaModel.value!!)

                    // Volvemos a la vista previa
                    resultado.value = true
                } catch (_: Exception) {
                    mensajeError.value = R.string.error_guardar_etiqueta
                }
            }
        }

        /**
         * Observa el resultado de la operación de guardar una etiqueta
         *
         * @return Un MutableLiveData que contiene un booleano que indica si la operación de guardar
         * una etiqueta se ha realizado correctamente (true) o no (false)
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun observarResultado(): MutableLiveData<Boolean> = resultado


        /**
         * Inserta una nueva etiqueta en la base de datos
         *
         * @param etiqueta La etiqueta a insertar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        private suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.insertarEtiqueta(etiqueta)
    }
