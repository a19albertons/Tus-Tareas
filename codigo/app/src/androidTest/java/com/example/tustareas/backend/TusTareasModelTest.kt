package com.example.tustareas.backend

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Notificacion
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.ListarTareasRepository
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.repository.WorkerRepository
import com.example.tustareas.util.DateHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Clase que gestiona las pruebas integración del modelo tus tareas
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TusTareasModelTest {
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
    lateinit var repositorioCrearTareas: CrearTareasRepository

    @Inject
    lateinit var repositorioListarTareas: ListarTareasRepository

    @Inject
    lateinit var repositorioTusTareas: TusTareasRepository

    lateinit var modeloTusTareasModel: TusTareasModel

    // Crear tareas
    val tareaCompleta =
        Tarea(
            nombre = "tarea completa",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            estado = Estado.COMPLETADA,
        )
    val tareaCompletaDto = TareaDTO(tareaCompleta, emptyList())
    val tareaIncompleta =
        Tarea(
            nombre = "tarea incompleta",
            prioridad = Prioridad.BAJA,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            estado = Estado.EN_TIEMPO,
        )
    val tareaNoCompletaDto = TareaDTO(tareaIncompleta, emptyList())

    // Configuramos la base de datos
    @Before
    fun creacionBd() {
        runBlocking {
            // Iniciar Hilt
            ruleHilt.inject()

            // Crear modelo
            modeloTusTareasModel = TusTareasModel(ApplicationProvider.getApplicationContext(), repositorioTusTareas)

            // Añadir tareas
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaCompletaDto)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaNoCompletaDto)
        }
    }

    // Destrucción bd temporal
    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    // Prueba de limpiar tareas completa
    @Test
    fun limpiarTareasCompletas() =
        runTest {
            // Limpia la tarea completa
            modeloTusTareasModel.limpiarTareasCompletas()

            // Vigilamos el live data como en estadisticas model test en su versión unitaria
            val liveData =
                repositorioListarTareas.obtenerTareasFiltradas(
                    Prioridad.entries.map { it }.toTypedArray(),
                    Estado.entries.map { it }.toTypedArray(),
                    "",
                    OrdenarTareas.FECHA_LIMITE_ASC,
                )
            liveData.observeForever { }

            // Comprueba que de las 2 taras de preuba en la bd en memoria solo queda 1
            val resultado = liveData.value
            assertEquals(
                1,
                resultado!!.size,
            )
        }

    // Prueba marcar notificaciones como leidas
    @Test
    fun marcarNotificacionComoLeida() =
        runTest {
            val notificacion =
                Notificacion(
                    titulo = "prueba",
                    mensaje = "prueba",
                    leido = false,
                    idTarea = 1,
                )
            // Fuerza que se lancen las notificaciones en el test
            WorkerRepository(db).anadirNotificacion(notificacion)

            // actualizacion de notificacion
            modeloTusTareasModel.notificaciones(
                intent =
                    Intent().apply {
                        putExtra("idNotificacion", 1)
                    },
            )

            // vigilar notificaciones
            val notificaicones = WorkerRepository(db).obtenerTodasLasNotificaciones()

            // Notificaciones comprobacion
            assertTrue(notificaicones[0].leido)
        }
}
