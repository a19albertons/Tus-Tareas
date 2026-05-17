package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.ListarEtiquetasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Clase que reperesenta al submodelo de listar etiquetas.
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de listar etiquetas
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class ListarEtiquetasModel
    @Inject
    constructor(
        application: Application,
        private val repository: ListarEtiquetasRepository,
    ) : AndroidViewModel(application) {
        // Filtro para etiquetas
        // Valor por defecto
        private val textoEtiqueta = MutableLiveData("")

        /**
         * Actualiza el valor del filtro de texto para las etiquetas
         *
         * @param texto El nuevo valor del filtro de texto para las etiquetas
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarTextoListadoEtiqueta(texto: String) {
            textoEtiqueta.value = texto
        }

        /**
         * Obtiene la lista de etiquetas filtradas según el texto del filtro
         *
         * @return Un LiveData que contiene una lista de etiquetas filtradas según el texto del filtro
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerEtiquetasFiltradas(): LiveData<List<Etiqueta>> =
            textoEtiqueta.switchMap { texto ->
                repository.obtenerEtiquetasFiltradas(texto)
            }
    }
