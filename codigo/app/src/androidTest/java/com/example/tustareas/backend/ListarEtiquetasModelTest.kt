package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Clase que gestiona las pruebas de integración de listar etiquetas model
 */
@RunWith(AndroidJUnit4::class)
class ListarEtiquetasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Variables comunes
    private lateinit var db: TusTareasDatabase
    private lateinit var repositorio: TusTareasRepository
    private lateinit var modelo: TusTareasModel


    private val diaReferencia = 1735686000000

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val aplicacion = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(contexto, TusTareasDatabase::class.java).build()
        repositorio = TusTareasRepository(db)
        modelo = TusTareasModel(aplicacion, repositorio)

        modelo.modificarEtiquetas.insertarEtiqueta(
            Etiqueta(
                id = 1,
                nombre = "etiqueta"
            )
        )

        modelo.modificarEtiquetas.insertarEtiqueta(
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
        val liveData = modelo.listarEtiquetas.obtenerEtiquetasFiltradas()
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
        modelo.listarEtiquetas.actualizarTextoListadoEtiqueta("etiqueta2")

        // Obtener datos
        val liveData = modelo.listarEtiquetas.obtenerEtiquetasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado?.size == 1)
        assert(resultado?.first()?.nombre == "etiqueta2")
    }

}