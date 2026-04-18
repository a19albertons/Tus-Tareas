package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * Clase que gestiona las pruebas de integracióndel proyecto detalles model
 */
@RunWith(AndroidJUnit4::class)
class ProyectoDetallesModelTest {
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

        // Unas tareas y etiquetas para las pruebas
        val tarea1 = Tarea(
            id = 1,
            nombre = "tarea 1",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.Completada
        )
        val tareaDTO1 = TareaDTO(tarea1, emptyList())
        val tarea2 = Tarea(
            id = 2,
            nombre = "tarea 2",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.Completada
        )
        val tareaDTO2 = TareaDTO(tarea2, emptyList())
        val etiqueta1 = Etiqueta(
            id = 1,
            nombre = "etiqueta 1"
        )
        val etiqueta2 = Etiqueta(
            id = 2,
            nombre = "etiqueta 2"
        )

        // Insercion
        modelo.modificarTareas.insertarTareaConEtiqueta(tareaDTO1)
        modelo.modificarTareas.insertarTareaConEtiqueta(tareaDTO2)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta1)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta2)

        // Crear proyecto
        val proyecto = Proyecto(
            nombre = "Proyecto 1",
            descripcion = "descripcion",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia)
        )
        val proyectoDTO = ProyectoDTO(
            proyecto,
            listOf(etiqueta1, etiqueta2),
            listOf(tarea1, tarea2)
        )
        // Insertar
        modelo.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    @Test
    fun obtenerProyectoPorId() {
        // Obtener datos
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.proyecto.nombre == "Proyecto 1")
    }

    @Test
    fun eliminarProyectoConTareaYEtiqueta() = runTest {
        // Obtener datos
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Eliminar proyecto
        modelo.proyectoDetalles.eliminarProyectoConTareaYEtiqueta(liveData.value!!)

        // Obtener datos actualizados
        val liveData2 = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData2.observeForever {  }


        // Resultado
        val resultado = liveData2.value
        assert(resultado?.proyecto == null)
    }
}