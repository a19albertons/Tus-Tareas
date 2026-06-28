package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.R
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.ListarEtiquetasModel
import com.example.tustareas.modelView.ModificarEtiquetasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.ListarEtiquetasRepository
import com.example.tustareas.repository.ModificarEtiquetasRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Clase que gestiona las pruebas de integracion de modificar etiquetas model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ModificarEtiquetasModelTest {
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
    lateinit var repositorioModificarEtiqueta: ModificarEtiquetasRepository

    @Inject
    lateinit var repositorioCrearEtiqueta: CrearEtiquetasRepository

    lateinit var modeloModificarEtiquetas: ModificarEtiquetasModel

    @Inject
    lateinit var listarEtiquetasRepositorio: ListarEtiquetasRepository

    lateinit var modeloListarEtiquetas: ListarEtiquetasModel

    private val diaReferencia = 1735689600000L

    // Etiquetas base
    val etiqueta1Base = Etiqueta(
        id = 1,
        nombre = "etiqueta",
    )

    val etiqueta2Base = Etiqueta(
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
            modeloListarEtiquetas = ListarEtiquetasModel(ApplicationProvider.getApplicationContext(), listarEtiquetasRepositorio)
            modeloModificarEtiquetas = ModificarEtiquetasModel(ApplicationProvider.getApplicationContext(), repositorioModificarEtiqueta)

            repositorioCrearEtiqueta.insertarEtiqueta(
                etiqueta1Base
            )

            repositorioCrearEtiqueta.insertarEtiqueta(
                etiqueta2Base
            )
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun modificarEtiqueta() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = etiqueta1Base.copy()

            // Definir una etiqueta
            modeloModificarEtiquetas.definirEtiqueta(etiqueta)

            // Modificar la etiqueta
            modeloModificarEtiquetas.modificarEtiqueta(
                "Etiqueta 1",
                "Descripción de la etiqueta 1",
            )

            // Comprobación del resultado
            assertTrue(modeloModificarEtiquetas.observarResultado().value == true)
        }

    @Test
    fun modificarEtiquetaNoValida() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = etiqueta1Base.copy()

            // Definir una etiqueta
            modeloModificarEtiquetas.definirEtiqueta(etiqueta)

            // Modificar la etiqueta
            modeloModificarEtiquetas.modificarEtiqueta(
                "",
                "Descripción de la etiqueta 1",
            )

            // Comprobación del resultado
            assertEquals(modeloModificarEtiquetas.observarMensajeError().value, R.string.error_modificar_etiqueta)
            assertTrue(modeloModificarEtiquetas.observarResultado().value == false)
        }

    @Test
    fun observarEtiqueta() =
        runTest {
            // Definición etiqueta de prueba
            val etiqueta = etiqueta1Base.copy()

            // Definir una etiqueta
            modeloModificarEtiquetas.definirEtiqueta(etiqueta)

            // Comprobación del resultado
            assertEquals(etiqueta1Base,modeloModificarEtiquetas.observarEtiqueta().value)
        }
}
