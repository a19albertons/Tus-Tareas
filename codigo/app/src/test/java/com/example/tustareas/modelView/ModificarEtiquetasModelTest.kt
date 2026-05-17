package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.R
import com.example.tustareas.helper.MainDispatcherRule
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

    @get:Rule
    val mainRule = MainDispatcherRule()

    // Definición repositorio
    val modificarEtiquetasRepository = Mockito.mock(ModificarEtiquetasRepository::class.java)

    // Definición modelo
    val modificarEtiquetasModel =
        ModificarEtiquetasModel(Application(), modificarEtiquetasRepository)

    @Test
    fun definirEtiquetayObservarEtiqueta() {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modificarEtiquetasModel.definirEtiqueta(etiqueta)

        // Comprobación del resultado
        assert(modificarEtiquetasModel.observarEtiqueta().value == etiqueta)
    }

    @Test
    fun tituloDialogoNueva() {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modificarEtiquetasModel.definirEtiqueta(etiqueta)

        // Comprobación del resultado
        assert(modificarEtiquetasModel.tituloDialogo() == R.string.confirmar_guardar_etiqueta)
    }

    @Test
    fun tituloDialogoExistente() {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modificarEtiquetasModel.definirEtiqueta(etiqueta)

        // Comprobación del resultado
        assert(modificarEtiquetasModel.tituloDialogo() == R.string.confirmar_modificar_etiqueta)
    }

    // Guardado de nuevas etiquetas
    @Test
    fun guardarEtiqueta() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            modificarEtiquetasModel.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            modificarEtiquetasModel.guardarYModificarEtiqueta("Etiqueta 1", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            Mockito.verify(modificarEtiquetasRepository).insertarEtiqueta(etiqueta)

            // Valor del resultado
            assert(modificarEtiquetasModel.observarResultado().value == true)
        }

    // Guardado de nuevas etiquetas no valida
    @Test
    fun guardarEtiquetaInvalida() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            modificarEtiquetasModel.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            modificarEtiquetasModel.guardarYModificarEtiqueta(" ", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            assert(modificarEtiquetasModel.observarMensajeError().value == R.string.error_guardar_etiqueta)

            // Valor del resultado
            assert(modificarEtiquetasModel.observarResultado().value == false)
        }

    // Guardado de nuevas etiquetas
    @Test
    fun guardarEtiquetaExistente() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            modificarEtiquetasModel.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            modificarEtiquetasModel.guardarYModificarEtiqueta("Etiqueta 1", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            Mockito.verify(modificarEtiquetasRepository).modificarEtiqueta(etiqueta)

            // Valor del resultado
            assert(modificarEtiquetasModel.observarResultado().value == true)
        }

    // Guardado de nuevas etiquetas no valida
    @Test
    fun guardarEtiquetaExistenteInvalida() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            modificarEtiquetasModel.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            modificarEtiquetasModel.guardarYModificarEtiqueta(" ", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            assert(modificarEtiquetasModel.observarMensajeError().value == R.string.error_modificar_etiqueta)

            // Valor del resultado
            assert(modificarEtiquetasModel.observarResultado().value == false)
        }
}
