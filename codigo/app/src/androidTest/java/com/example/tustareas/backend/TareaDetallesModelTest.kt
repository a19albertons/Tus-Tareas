package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.TareaDetallesModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.TareaDetallesRepository
import com.example.tustareas.util.DateHelper
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
import java.util.Date
import javax.inject.Inject

/**
 * Clase que gestiona las pruebas de intregración de tarea detalles model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TareaDetallesModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para corutinas
    @get:Rule
    val ruleCoroutines = MainDispatcherRule()

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var repositorioCrearTareas: CrearTareasRepository

    @Inject
    lateinit var repositorioCrearEtiquetas: CrearEtiquetasRepository

    @Inject
    lateinit var repositorioDetallesTarea: TareaDetallesRepository

    lateinit var modeloDetallesTarea: TareaDetallesModel

    private val diaReferencia = 1735689600000L

    // Tarea sin fecha limite, sin prioridad y en tiempo
    val tarea1 =
        Tarea(
            id = 1,
            nombre = "tarea1",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia - 86400000),
            fechaLimite = null,
            estado = Estado.EN_TIEMPO,
        )
    val tarea1DTO = TareaDTO(tarea1, emptyList())

    // Tarea con fecha limite, pero no retrasada, prioridad baja, y más vieja en creación
    val tarea2 =
        Tarea(
            id = 2,
            nombre = "tarea2",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia),
            fechaLimite = Date(diaReferencia + 86400000), // Un día después
            estado = Estado.EN_TIEMPO,
        )
    val tarea2DTO = TareaDTO(tarea2, emptyList())

    // Completada, priroridad alta, descripcion
    val tareaHoy =
        Tarea(
            id = 3,
            nombre = "tareaHoy",
            descripcion = "descripcion",
            prioridad = Prioridad.ALTA,
            fechaCreacion = Date(DateHelper.fechaMediaNocheUTC().time - 86400000),
            fechaLimite = Date(diaReferencia), // Hoy
            estado = Estado.COMPLETADA,
        )
    val etiqueta =
        Etiqueta(
            id = 1,
            nombre = "etiqueta",
        )
    val tareaHoyDTO = TareaDTO(tareaHoy, listOf(etiqueta))

    // Tarea retrasada, prioridad media y retrasada
    val tareaRETRASADA =
        Tarea(
            id = 4,
            nombre = "tareaRetrasada",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(diaReferencia - 86400000), // Un día antes
            estado = Estado.RETRASADA,
        )
    val tareaRetrasadaDTO = TareaDTO(tareaRETRASADA, emptyList())

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Inyectar dependencias
            ruleHilt.inject()

            // Crear modelos
            modeloDetallesTarea = TareaDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesTarea)

            // Insertar tareas y etiqueta
            repositorioCrearTareas.insertarTareaConEtiqueta(tarea1DTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tarea2DTO)
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)
        }

    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    // Prueba de obtención de tarea por id
    @Test
    fun obtenerTarea1() =
        runTest {
            // Obtener referencia
            val liveData = modeloDetallesTarea.obtenerTareaDTOPorID(1)
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assertEquals("tarea1", resultado!!.tarea.nombre)
        }

    // Prueba de eliminar una tarea
    @Test
    fun eliminarTarea1() =
        runTest {
            // Obtener referencia
            val liveData = modeloDetallesTarea.obtenerTareaDTOPorID(1)
            liveData.observeForever { }

            // Eliminar tarea
            val eliminar = liveData.value
            modeloDetallesTarea.eliminarTarea(eliminar!!.tarea)

            // Obtener referencia nueva
            val liveData2 = modeloDetallesTarea.obtenerTareaDTOPorID(1)
            liveData2.observeForever { }

            // Resultado
            val resultado = liveData2.value
            assertEquals(null, resultado?.tarea)
        }
}
