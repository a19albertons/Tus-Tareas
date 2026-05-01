package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que reperesenta al submodelo de listar etiquetas.
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ListarEtiquetasModel(private val repository: TusTareasRepository) {
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
    fun obtenerEtiquetasFiltradas() : LiveData<List<Etiqueta>> = textoEtiqueta.switchMap {
            texto ->
        repository.listarEtiquetas.obtenerEtiquetasFiltradas(texto)
    }
}