package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.ModificarEtiquetasRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


/**
 * Clase que tiene las pruebas unitarias de modificar etiquetas model
 */
class ModificarEtiquetasModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val modificarEtiquetasRepository = Mockito.mock(ModificarEtiquetasRepository::class.java)

    // Definición modelo
    val modificarEtiquetasModel = ModificarEtiquetasModel(Application(), modificarEtiquetasRepository)

    @Test
    fun insertarEtiqueta() = runTest {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definición respuestas
        Mockito.`when`(modificarEtiquetasRepository.insertarEtiqueta(etiqueta))
            .thenReturn(Unit)

        // Comprobación del resultado
        modificarEtiquetasModel.insertarEtiqueta(etiqueta)
        Mockito.verify(modificarEtiquetasRepository).insertarEtiqueta(etiqueta)
    }

    @Test
    fun modificarEtiqueta() = runTest {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definición respuestas
        Mockito.`when`(modificarEtiquetasRepository.modificarEtiqueta(etiqueta))
            .thenReturn(Unit)

        // Comprobación del resultado
        modificarEtiquetasModel.modificarEtiqueta(etiqueta)
        Mockito.verify(modificarEtiquetasRepository).modificarEtiqueta(etiqueta)
    }
}