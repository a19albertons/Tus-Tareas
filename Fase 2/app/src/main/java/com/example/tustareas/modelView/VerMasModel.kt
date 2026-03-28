package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository

class VerMasModel(private val repository: TusTareasRepository) {
    // Filtro para VerMás
    private val textoVerMas = MutableLiveData("")
    fun actualizarTextoVerMas(texto: String) {
        textoVerMas.value = texto
    }
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