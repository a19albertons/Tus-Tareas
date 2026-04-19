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
 * Clase que gestiona las pruebas de integracion de modificar etiquetas model
 */
@RunWith(AndroidJUnit4::class)
class ModificarEtiquetasModelTest {
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

    // Insertar etiqueta nueva
    @Test
    fun insertarEtiqueta() = runTest {
        // Crear e insertar etiqueta
        val etiquetaNueva = Etiqueta(
            nombre = "prueba",
            descripcion = "descripcion"
        )
        modelo.modificarEtiquetas.insertarEtiqueta(etiquetaNueva)

        // obtener datos
        val liveData = modelo.listarEtiquetas.obtenerEtiquetasFiltradas()
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
        val liveData = modelo.listarEtiquetas.obtenerEtiquetasFiltradas()
        liveData.observeForever {  }

        // Modificacion
        val etiquetaModificar = liveData.value!!.last()
        etiquetaModificar.nombre = "modificado"
        etiquetaModificar.descripcion = "descripcion modificada"
        modelo.modificarEtiquetas.modificarEtiqueta(etiquetaModificar)

        // Obtener datos actualziados
        val liveData2 = modelo.listarEtiquetas.obtenerEtiquetasFiltradas()
        liveData2.observeForever {  }

        // resultado
        val resultado = liveData2.value
        assert(resultado?.size == 2)
        assert(resultado?.last()?.nombre == "modificado")
        assert(resultado?.last()?.descripcion == "descripcion modificada")
    }
}