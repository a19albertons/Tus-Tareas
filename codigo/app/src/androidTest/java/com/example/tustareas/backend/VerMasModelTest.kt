package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.ListarTareasModel
import com.example.tustareas.modelView.VerMasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.ListarTareasRepository
import com.example.tustareas.repository.VerMasRepository
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
 * Clase que contiene los test de integración de ver mas model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class VerMasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Necesario para saltarse los scope que se ejecutan en hilos secundarios
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var repositorioCrearTareas: CrearTareasRepository

    @Inject
    lateinit var repositorioListarTareas: ListarTareasRepository

    lateinit var modeloListarTareas: ListarTareasModel

    @Inject
    lateinit var repositorioVerMas: VerMasRepository

    lateinit var modeloVerMas: VerMasModel

    // En esta clase de pruebas no se usa
    private val diaReferencia = 1735689600000L

    // Tarea sin fecha limite
    val tarea1 =
        Tarea(
            id = 1,
            nombre = "tarea1",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = null,
            estado = Estado.EN_TIEMPO,
        )
    val tarea1DTO = TareaDTO(tarea1, emptyList())
    // Tarea con fecha limite, pero no retrasada
    val tarea2 =
        Tarea(
            id = 2,
            nombre = "tarea2",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
            estado = Estado.EN_TIEMPO,
        )
    val tarea2DTO = TareaDTO(tarea2, emptyList())
    // Tarea hoy
    val tareaHoy =
        Tarea(
            id = 3,
            nombre = "tareaHoy",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = DateHelper.fechaMediaNocheUTC(), // Hoy
            estado = Estado.EN_TIEMPO,
        )
    val tareaHoyDTO = TareaDTO(tareaHoy, emptyList())
    // Tarea retrasada
    val tareaRETRASADA =
        Tarea(
            id = 4,
            nombre = "tareaRetrasada",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time - 86400000), // Un día antes
            // Es el campo se mira.
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
            modeloListarTareas = ListarTareasModel(ApplicationProvider.getApplicationContext(), repositorioListarTareas)
            modeloVerMas = VerMasModel(ApplicationProvider.getApplicationContext(), repositorioVerMas)



            // Insertar tareas
            repositorioCrearTareas.insertarTareaConEtiqueta(tarea1DTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tarea2DTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)
        }

    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    // test modificar tarea (marcar como completada)
    @Test
    fun marcarTareaComoCompletada() =
        runTest {
            // Obtener datos 1
            val liveData = modeloListarTareas.obtenerTareasFiltradas()
            liveData.observeForever { }

            // Obtenemos la tarea a modificar
            val modificada = liveData.value?.get(0)

            val booleano = true
            modeloVerMas.actualizarEstado(modificada!!, booleano)

            // Obtener datos 2
            val liveData2 = modeloListarTareas.obtenerTareasFiltradas()
            liveData2.observeForever { }

            // Resultado
            val resultado = liveData2.value
            assertEquals(Estado.COMPLETADA, resultado!!.first().estado)
        }

    // Tareas para hoy -- cantidad
    @Test
    fun tareaParaHoy1() {
        val liveData = modeloVerMas.obtenerTareasTerminanDiaEspecificoConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assertEquals(1, resultado!!.size)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaHoy2() {
        val liveData = modeloVerMas.obtenerTareasTerminanDiaEspecificoConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assertEquals("tareaHoy", resultado!![0].nombre)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaHoy3() {
        modeloVerMas.actualizarTextoVerMas("TAREAHOY")
        val liveData = modeloVerMas.obtenerTareasTerminanDiaEspecificoConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assertEquals("tareaHoy", resultado!![0].nombre)
    }

    // Tareas para retrasadas -- cantidad
    @Test
    fun tareaParaRetrasadas1() {
        val liveData = modeloVerMas.obtenerTareasRetrasadasConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assertEquals(1, resultado!!.size)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaRetrasads2() {
        val liveData = modeloVerMas.obtenerTareasRetrasadasConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assertEquals("tareaRetrasada", resultado!![0].nombre)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaRetrasads3() {
        modeloVerMas.actualizarTextoVerMas("TAREARETRASADA")
        val liveData = modeloVerMas.obtenerTareasRetrasadasConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assertEquals("tareaRetrasada", resultado!![0].nombre)
    }

    // Tareas futuras -- cantidad
    @Test
    fun tareaParaFuturo1() {
        val liveData = modeloVerMas.obtenerTareasProximasConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assertEquals(2, resultado!!.size)
    }

    // Tareas futuras -- Nombre tarea
    @Test
    fun tareaParaFuturo2() {
        val liveData = modeloVerMas.obtenerTareasProximasConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assertEquals("tarea1", resultado!![0].nombre)
    }

    @Test
    fun tareaParaFuturo3() {
        val liveData = modeloVerMas.obtenerTareasProximasConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assertEquals("tarea2", resultado!![1].nombre)
    }

    @Test
    fun tareaParaFuturo4() {
        modeloVerMas.actualizarTextoVerMas("TAREA2")
        val liveData = modeloVerMas.obtenerTareasProximasConFiltro()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assertEquals("tarea2", resultado!![0].nombre)
    }
}
