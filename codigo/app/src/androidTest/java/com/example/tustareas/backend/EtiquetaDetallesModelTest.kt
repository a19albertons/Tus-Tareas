package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelView.EtiquetaDetallesModel
import com.example.tustareas.modelView.ListarEtiquetasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.EtiquetaDetallesRepository
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
 * Clase que gestiona las pruebas de integracion de etiqueta detalles model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class EtiquetaDetallesModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Regla necesaria para hilt
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var crearEtiquetas: CrearEtiquetasRepository

    @Inject
    lateinit var etiquetaDetallesRepository: EtiquetaDetallesRepository

    lateinit var etiquetaDetallesModel: EtiquetaDetallesModel

    @Inject
    lateinit var listarEtiquetasRepository: ListarEtiquetasRepository

    lateinit var listarEtiquetasModel: ListarEtiquetasModel

    private val diaReferencia = 1735689600000L

    // Etiquetas comunes
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
            // Inyección de dependencias
            ruleHilt.inject()

            // Creacion de modelos
            etiquetaDetallesModel = EtiquetaDetallesModel(ApplicationProvider.getApplicationContext(), etiquetaDetallesRepository)
            listarEtiquetasModel = ListarEtiquetasModel(ApplicationProvider.getApplicationContext(), listarEtiquetasRepository)

            // Añadir datos de prueba
            crearEtiquetas.insertarEtiqueta(
                etiqueta1Base
            )

            crearEtiquetas.insertarEtiqueta(
                etiqueta2Base
            )
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    // Test de obtener una etiqueta por id
    @Test
    fun obtenerEtiquetaPorId() =
        runTest {
            // Obtener referencia
            val liveData = etiquetaDetallesModel.obtenerEtiquetaPorID(2)
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assertEquals("etiqueta2", resultado?.nombre)
        }

    // Prueba de eliminar una etiqueta
    @Test
    fun eliminarEtiqueta() =
        runTest {
            // Obtener referencia
            val liveData = etiquetaDetallesModel.obtenerEtiquetaPorID(2)
            liveData.observeForever { }

            // eliminacion
            val eliminarEtiqueta = liveData.value
            etiquetaDetallesModel.eliminarEtiqueta(eliminarEtiqueta!!)

            // Obtener referencia
            val liveData2 = listarEtiquetasModel.obtenerEtiquetasFiltradas()
            liveData2.observeForever { }

            // Resultado
            val resultado = liveData2.value
            assertEquals(1, resultado!!.size)
            assertEquals("etiqueta", resultado.first().nombre)
        }
}
