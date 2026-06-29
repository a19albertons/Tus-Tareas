package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.R
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.CrearEtiquetasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.ListarEtiquetasRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Clase que gestiona las pruebas de integracion de crear etiquetas model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CrearEtiquetasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para las corutinas
    @get:Rule
    val ruleCoroutines = MainDispatcherRule()

    // Necesario para Hilt
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var repositorioCrearEtiqueta: CrearEtiquetasRepository

    lateinit var modeloCrearEtiquetas: CrearEtiquetasModel

    @Inject
    lateinit var listarEtiquetasRepositorio: ListarEtiquetasRepository

    // Valores base comunes
    val etiquetaBase1 =
        Etiqueta(
            id = 1,
            nombre = "etiqueta",
        )

    val etiquetaBase2 =
        Etiqueta(
            id = 2,
            nombre = "etiqueta2",
        )

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Inyectamos las dependencias
            ruleHilt.inject()

            // Crear modelo
            modeloCrearEtiquetas = CrearEtiquetasModel(ApplicationProvider.getApplicationContext(), repositorioCrearEtiqueta)

            repositorioCrearEtiqueta.insertarEtiqueta(
                etiquetaBase1,
            )

            repositorioCrearEtiqueta.insertarEtiqueta(
                etiquetaBase2,
            )
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    // Guardado de nuevas etiquetas
    @Test
    fun guardarEtiqueta() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            modeloCrearEtiquetas.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            modeloCrearEtiquetas.guardarEtiqueta("Etiqueta 1", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            assertEquals(true, modeloCrearEtiquetas.observarResultado().value)
        }

    // Guardado de nuevas etiquetas no valida
    @Test
    fun guardarEtiquetaNoValida() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

            // Definir una etiqueta
            modeloCrearEtiquetas.definirEtiqueta(etiqueta)

            // Guardar la etiqueta
            modeloCrearEtiquetas.guardarEtiqueta("", "Descripción de la etiqueta 1")

            // Comprobación del resultado
            assertEquals(R.string.error_guardar_etiqueta, modeloCrearEtiquetas.observarMensajeError().value)
            assertEquals(false, modeloCrearEtiquetas.observarResultado().value)
        }

    // Test que comprueba el funionamiento de definir la et
    @Test
    fun observarEtiqueta() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = etiquetaBase1.copy()

            // Definir una etiqueta
            modeloCrearEtiquetas.definirEtiqueta(etiqueta)

            // Comprobación del resultado
            assertEquals(etiqueta, modeloCrearEtiquetas.observarEtiqueta().value)
        }
}
