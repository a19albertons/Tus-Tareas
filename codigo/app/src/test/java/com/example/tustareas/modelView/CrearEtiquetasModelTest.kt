package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.R
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.CrearEtiquetasRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

/**
 * Clase que tiene las pruebas unitarias de crear etiquetas model
 */
class CrearEtiquetasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainRule = MainDispatcherRule()

    // Definición repositorio
    val crearEtiquetasRepository = Mockito.mock(CrearEtiquetasRepository::class.java)

    // Definición modelo
    val crearEtiquetasModel =
        CrearEtiquetasModel(Application(), crearEtiquetasRepository)

    @Test
    fun definirEtiquetayObservarEtiqueta() {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        crearEtiquetasModel.definirEtiqueta(etiqueta)

        // Comprobación del resultado
        assert(crearEtiquetasModel.observarEtiqueta().value == etiqueta)
    }

    // Guardado de nuevas etiquetas
    @Test
    fun guardarEtiqueta() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            crearEtiquetasModel.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            crearEtiquetasModel.guardarEtiqueta("Etiqueta 1", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            Mockito.verify(crearEtiquetasRepository).insertarEtiqueta(etiqueta)

            // Valor del resultado
            assert(crearEtiquetasModel.observarResultado().value == true)
        }

    // Guardado de nuevas etiquetas no valida
    @Test
    fun guardarEtiquetaInvalida() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            crearEtiquetasModel.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            crearEtiquetasModel.guardarEtiqueta(" ", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            assert(crearEtiquetasModel.observarMensajeError().value == R.string.error_guardar_etiqueta)

            // Valor del resultado
            assert(crearEtiquetasModel.observarResultado().value == false)
        }
}
