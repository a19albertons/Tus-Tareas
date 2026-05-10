package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelView.ListarEtiquetasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.ListarEtiquetasRepository
import com.example.tustareas.repository.ModificarEtiquetasRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
    lateinit var repositorioEtiquetas: ModificarEtiquetasRepository

    @Inject
    lateinit var ListarEtiquetaRepositorio : ListarEtiquetasRepository


    lateinit var modelo: ListarEtiquetasModel


    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        // Aplicar regla
        ruleHilt.inject()

        // Inicializar modelo manualmente
        modelo = ListarEtiquetasModel(ApplicationProvider.getApplicationContext(), ListarEtiquetaRepositorio)

        repositorioEtiquetas.insertarEtiqueta(
            Etiqueta(
                id = 1,
                nombre = "etiqueta"
            )
        )

        repositorioEtiquetas.insertarEtiqueta(
            Etiqueta(
                id = 2,
                nombre = "etiqueta2"
            )
        )
    }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    @Test
    fun obtenerTodas() = runTest {
        // Obtener datos
        val liveData = modelo.obtenerEtiquetasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado?.size == 2)
        assert(resultado?.first()?.nombre == "etiqueta")
        assert(resultado?.last()?.nombre == "etiqueta2")
    }

    @Test
    fun obtenerPorNombre() = runTest {
        // Configuración
        modelo.actualizarTextoListadoEtiqueta("etiqueta2")

        // Obtener datos
        val liveData = modelo.obtenerEtiquetasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado?.size == 1)
        assert(resultado?.first()?.nombre == "etiqueta2")
    }

}