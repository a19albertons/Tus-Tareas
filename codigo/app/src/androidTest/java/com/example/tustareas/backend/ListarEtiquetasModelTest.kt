package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelView.ListarEtiquetasModel
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
 * Clase que gestiona las pruebas de integración de listar etiquetas model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListarEtiquetasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var crearRepositorioEtiquetas: CrearEtiquetasRepository

    @Inject
    lateinit var listarEtiquetaRepositorio: ListarEtiquetasRepository

    lateinit var modelo: ListarEtiquetasModel

    private val diaReferencia = 1735689600000L

    // Etiquetas base
    val etiqueta1Base =
        Etiqueta(
            id = 1,
            nombre = "etiqueta",
        )
    val etiqueta2Base =
        Etiqueta(
            id = 2,
            nombre = "etiqueta2",
        )

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Aplicar regla
            ruleHilt.inject()

            // Inicializar modelo manualmente
            modelo = ListarEtiquetasModel(ApplicationProvider.getApplicationContext(), listarEtiquetaRepositorio)

            crearRepositorioEtiquetas.insertarEtiqueta(
                etiqueta1Base,
            )

            crearRepositorioEtiquetas.insertarEtiqueta(
                etiqueta2Base,
            )
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun obtenerTodas() =
        runTest {
            // Obtener datos
            val liveData = modelo.obtenerEtiquetasFiltradas()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assertEquals(2, resultado?.size)
            assertEquals("etiqueta", resultado?.first()?.nombre)
            assertEquals("etiqueta2", resultado?.last()?.nombre)
        }

    @Test
    fun obtenerPorNombre() =
        runTest {
            // Configuración
            modelo.actualizarTextoListadoEtiqueta("etiqueta2")

            // Obtener datos
            val liveData = modelo.obtenerEtiquetasFiltradas()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assertEquals(1, resultado?.size)
            assertEquals("etiqueta2", resultado?.first()?.nombre)
        }
}
