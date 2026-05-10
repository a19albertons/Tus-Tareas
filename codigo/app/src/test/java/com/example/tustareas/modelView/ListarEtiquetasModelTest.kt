package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.ListarEtiquetasRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import kotlin.collections.listOf

/**
 * Clase que tiene las pruebas unitarias de inicio model
 */
class ListarEtiquetasModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val listarEtiquetasRepository = Mockito.mock(ListarEtiquetasRepository::class.java)

    // Definición modelo
    val listarEtiquetasModel = ListarEtiquetasModel(Application(), listarEtiquetasRepository)

    @Test
    fun obtenerEtiquetasFiltradas() {
        // Definición respuestas
        Mockito.`when`(listarEtiquetasRepository.obtenerEtiquetasFiltradas(""))
            .thenReturn(MutableLiveData(emptyList()))

        // Obtener dato del observer
        val liveData = listarEtiquetasModel.obtenerEtiquetasFiltradas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.isEmpty())
    }

    @Test
    fun obtenerEtiquetasFiltradas2() {
        // Definición respuestas
        Mockito.`when`(listarEtiquetasRepository.obtenerEtiquetasFiltradas(""))
            .thenReturn(MutableLiveData(listOf(
                Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1"),
                Etiqueta(2, "Etiqueta 2", "Descripción de la etiqueta 2")
            )))

        // Obtener dato del observer
        val liveData = listarEtiquetasModel.obtenerEtiquetasFiltradas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.size == 2)
    }

}