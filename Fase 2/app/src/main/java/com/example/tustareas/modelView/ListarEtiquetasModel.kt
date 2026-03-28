package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que reperesenta al submodelo de listar etiquetas
 */
class ListarEtiquetasModel(private val repository: TusTareasRepository) {
    // Filtro para etiquetas
    // Valor por defecto
    private val textoEtiqueta = MutableLiveData("")
    // Actualiza el valor
    fun actualizarTextoListadoEtiqueta(texto: String) {
        textoEtiqueta.value = texto
    }
    // Llama a repositorio con el filtro
    fun obtenerEtiquetasFiltradas() : LiveData<List<Etiqueta>> = textoEtiqueta.switchMap {
            texto ->
        repository.listarEtiquetasModel.obtenerEtiquetasFiltradas(texto)
    }
}