package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * Clase que gestiona las pruebas de intregración de tarea detalles model
 */
@RunWith(AndroidJUnit4::class)
class TareaDetallesModelTest {
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

        // Tarea sin fecha limite, sin prioridad y en tiempo
        val tarea1 = Tarea(
            id = 1,
            nombre = "tarea1",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = Date(diaReferencia - 86400000),
            fechaLimite = null,
            estado = Estado.EnTiempo
        )
        val tarea1DTO = TareaDTO(tarea1, emptyList())
        // Tarea con fecha limite, pero no retrasada, prioridad baja, y más vieja en creación
        val tarea2 = Tarea(
            nombre = "tarea2",
            prioridad = Prioridad.Baja,
            fechaCreacion = Date(diaReferencia),
            fechaLimite = Date(diaReferencia + 86400000), // Un día después
            estado = Estado.EnTiempo
        )
        val tarea2DTO = TareaDTO(tarea2, emptyList())
        // Completada, priroridad alta, descripcion
        val tareaHoy = Tarea(
            nombre = "tareaHoy",
            descripcion = "descripcion",
            prioridad = Prioridad.Alta,
            fechaCreacion = Date(Date().time - 86400000),
            fechaLimite = Date(diaReferencia), // Hoy
            estado = Estado.Completada
        )
        val etiqueta = Etiqueta(
            // Id interno manual para base de pruebas
            id = 1,
            nombre = "etiqueta"
        )
        val tareaHoyDTO = TareaDTO(tareaHoy, listOf(etiqueta))
        // Tarea retrasada, prioridad media y retrasada
        val tareaRetrasada = Tarea(
            nombre = "tareaRetrasada",
            prioridad = Prioridad.Media,
            fechaCreacion = Date(),
            fechaLimite = Date(diaReferencia - 86400000), // Un día antes
            estado = Estado.Retrasada
        )
        val tareaRetrasadaDTO = TareaDTO(tareaRetrasada, emptyList())

        // Insertar tareas y etiqueta
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea1DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea2DTO)
        repositorio.modificacionEtiqueta.insertarEtiqueta(etiqueta)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaHoyDTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)


    }

    // Prueba de obtención de tarea por id
    @Test
    fun obtenerTarea1() = runTest {
        // Obtener referencia
        val liveData = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.tarea.nombre == "tarea1")
    }

    // Prueba de eliminar una tarea
    @Test
    fun eliminarTarea1() = runTest {
        // Obtener referencia
        val liveData = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData.observeForever {  }

        // Eliminar tarea
        val eliminar = liveData.value
        modelo.tareaDetalles.eliminarTarea(eliminar!!.tarea)

        // Obtener referencia nueva
        val liveData2 = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData2.observeForever {  }

        // Resultado
        val resultado = liveData2.value
        assert(resultado?.tarea == null)
    }
}