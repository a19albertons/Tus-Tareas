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

    
    lateinit var modeloModificarEtiquetas: ModificarEtiquetasModel
    
    
    @Inject
    lateinit var listarEtiquetasRepositorio: ListarEtiquetasRepository

    
    lateinit var modeloListarEtiquetas: ListarEtiquetasModel





    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        // Inyectamos las dependencias
        ruleHilt.inject()

        // Crear modelo
        modeloListarEtiquetas = ListarEtiquetasModel(ApplicationProvider.getApplicationContext(), listarEtiquetasRepositorio)
        modeloModificarEtiquetas = ModificarEtiquetasModel(ApplicationProvider.getApplicationContext(), repositorioModificarEtiqueta)

        repositorioModificarEtiqueta.insertarEtiqueta(
            Etiqueta(
                id = 1,
                nombre = "etiqueta"
            )
        )

        repositorioModificarEtiqueta.insertarEtiqueta(
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
    fun tituloDialogoNueva() {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modeloModificarEtiquetas.definirEtiqueta(etiqueta)

        // Comprobación del resultado
        assert(modeloModificarEtiquetas.tituloDialogo() == R.string.confirmar_guardar_etiqueta)
    }

    @Test
    fun tituloDialogoExistente() {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modeloModificarEtiquetas.definirEtiqueta(etiqueta)

        // Comprobación del resultado
        assert(modeloModificarEtiquetas.tituloDialogo() == R.string.confirmar_modificar_etiqueta)
    }

    // Guardado de nuevas etiquetas
    @Test
    fun guardarEtiqueta() = runTest {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modeloModificarEtiquetas.definirEtiqueta(etiqueta)

        // Guardar la etiqueta
        modeloModificarEtiquetas.guardarYModificarEtiqueta("Etiqueta 1", "Descripción de la etiqueta 1")

        // Comprobación del resultado
        assert(modeloModificarEtiquetas.observarResultado().value == true)
    }

    // Guardado de nuevas etiquetas no valida
    @Test
    fun guardarEtiquetaNoValida() = runTest {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(0, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modeloModificarEtiquetas.definirEtiqueta(etiqueta)

        // Guardar la etiqueta
        modeloModificarEtiquetas.guardarYModificarEtiqueta("", "Descripción de la etiqueta 1")

        // Comprobación del resultado
        assert(modeloModificarEtiquetas.observarMensajeError().value == R.string.error_guardar_etiqueta)
        assert(modeloModificarEtiquetas.observarResultado().value == false)
    }

    @Test
    fun modificarEtiqueta() = runTest {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modeloModificarEtiquetas.definirEtiqueta(etiqueta)

        // Modificar la etiqueta
        modeloModificarEtiquetas.guardarYModificarEtiqueta(
            "Etiqueta 1",
            "Descripción de la etiqueta 1"
        )

        // Comprobación del resultado
        assert(modeloModificarEtiquetas.observarResultado().value == true)
    }

    @Test
    fun modificarEtiquetaNoValida() = runTest {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modeloModificarEtiquetas.definirEtiqueta(etiqueta)

        // Modificar la etiqueta
        modeloModificarEtiquetas.guardarYModificarEtiqueta(
            "",
            "Descripción de la etiqueta 1"
        )

        // Comprobación del resultado
        assert(modeloModificarEtiquetas.observarMensajeError().value == R.string.error_modificar_etiqueta)
        assert(modeloModificarEtiquetas.observarResultado().value == false)
    }

    @Test
    fun observarEtiqueta() = runTest {
        // Definición etiqueta de prueba
        val etiqueta = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Definir una etiqueta
        modeloModificarEtiquetas.definirEtiqueta(etiqueta)

        // Comprobación del resultado
        assert(modeloModificarEtiquetas.observarEtiqueta().value == etiqueta)
    }

}