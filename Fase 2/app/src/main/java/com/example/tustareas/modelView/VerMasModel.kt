package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo del fragmento ver mas
 */
class VerMasModel(private val repository: TusTareasRepository) {
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
}