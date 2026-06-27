package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.InicioModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.InicioRepository
import com.example.tustareas.util.DateHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject

/**
 * Clase que contiene los test de integración de inicioModel
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class InicioModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Regla de Hilt para inyección de dependencias
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val diaReferencia = 1735689600000L

    // Variables a inyectar
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var repositorioCrearTarea: CrearTareasRepository

    @Inject
    lateinit var inicioRepository: InicioRepository

    lateinit var modelo: InicioModel

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Aplicar regla
            hiltRule.inject()

            // Inicializar modelo manualmente
            modelo = InicioModel(ApplicationProvider.getApplicationContext(), inicioRepository)

            // Tarea sin fecha limite
            val tarea1 =
                Tarea(
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
                    nombre = "tarea2",
                    prioridad = Prioridad.NO_ESTABLECIDO,
                    fechaCreacion = DateHelper.fechaMediaNocheUTC(),
                    fechaLimite = Date(diaReferencia + 86400000), // Un día después
                    estado = Estado.EN_TIEMPO,
                )
            val tarea2DTO = TareaDTO(tarea2, emptyList())
            // Tarea hoy
            val tareaHoy =
                Tarea(
                    nombre = "tareaHoy",
                    prioridad = Prioridad.NO_ESTABLECIDO,
                    fechaCreacion = DateHelper.fechaMediaNocheUTC(),
                    fechaLimite = Date(diaReferencia), // Hoy
                    estado = Estado.EN_TIEMPO,
                )
            val tareaHoyDTO = TareaDTO(tareaHoy, emptyList())
            // Tarea retrasada
            val tareaRETRASADA =
                Tarea(
                    nombre = "tareaRetrasada",
                    prioridad = Prioridad.NO_ESTABLECIDO,
                    fechaCreacion = DateHelper.fechaMediaNocheUTC(),
                    fechaLimite = Date(diaReferencia - 86400000), // Un día antes
                    // Es el campo se mira.
                    estado = Estado.RETRASADA,
                )
            val tareaRetrasadaDTO = TareaDTO(tareaRETRASADA, emptyList())

            // Insertar tareas
            repositorioCrearTarea.insertarTareaConEtiqueta(tarea1DTO)
            repositorioCrearTarea.insertarTareaConEtiqueta(tarea2DTO)
            repositorioCrearTarea.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioCrearTarea.insertarTareaConEtiqueta(tareaRetrasadaDTO)
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    // Tareas para hoy -- cantidad
    @Test
    fun tareaParaHoy1() {
        val liveData = modelo.obtenerTareasTerminanDiaEspecifico(Date(diaReferencia))
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!!.size == 1)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaHoy2() {
        val liveData = modelo.obtenerTareasTerminanDiaEspecifico(Date(diaReferencia))
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!![0].nombre == "tareaHoy")
    }

    // Tareas para retrasadas -- cantidad
    @Test
    fun tareaParaRetrasadas1() {
        val liveData = modelo.obtenerTareasRetrasadas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!!.size == 1)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaRetrasads2() {
        val liveData = modelo.obtenerTareasRetrasadas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![0].nombre == "tareaRetrasada")
    }

    // Tareas futuras -- cantidad
    @Test
    fun tareaParaFuturo1() {
        val liveData = modelo.obtenerTareasProximas(Date(diaReferencia))
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!!.size == 2)
    }

    // Tareas futuras -- Nombre tarea
    @Test
    fun tareaParaFuturo2() {
        val liveData = modelo.obtenerTareasProximas(Date(diaReferencia))
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![0].nombre == "tarea1")
    }

    @Test
    fun tareaParaFuturo3() {
        val liveData = modelo.obtenerTareasProximas(Date(diaReferencia))
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![1].nombre == "tarea2")
    }
}
