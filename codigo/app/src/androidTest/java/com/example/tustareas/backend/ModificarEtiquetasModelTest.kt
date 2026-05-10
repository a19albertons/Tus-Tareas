package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
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

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
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

    // Insertar etiqueta nueva
    @Test
    fun insertarEtiqueta() = runTest {
        // Crear e insertar etiqueta
        val etiquetaNueva = Etiqueta(
            nombre = "prueba",
            descripcion = "descripcion"
        )
        modeloModificarEtiquetas.insertarEtiqueta(etiquetaNueva)

        // obtener datos
        val liveData = modeloListarEtiquetas.obtenerEtiquetasFiltradas()
        liveData.observeForever {  }

        // resultado
        val resultado = liveData.value
        assert(resultado?.size == 3)
        assert(resultado?.last()?.nombre == "prueba")
    }

    // Modificar etiqueta nueva
    @Test
    fun modificarEtiqueta() = runTest {
        // modificar etiqueta
        val liveData = modeloListarEtiquetas.obtenerEtiquetasFiltradas()
        liveData.observeForever {  }

        // Modificacion
        val etiquetaModificar = liveData.value!!.last()
        etiquetaModificar.nombre = "modificado"
        etiquetaModificar.descripcion = "descripcion modificada"
        modeloModificarEtiquetas.modificarEtiqueta(etiquetaModificar)

        // Obtener datos actualziados
        val liveData2 = modeloListarEtiquetas.obtenerEtiquetasFiltradas()
        liveData2.observeForever {  }

        // resultado
        val resultado = liveData2.value
        assert(resultado?.size == 2)
        assert(resultado?.last()?.nombre == "modificado")
        assert(resultado?.last()?.descripcion == "descripcion modificada")
    }
}