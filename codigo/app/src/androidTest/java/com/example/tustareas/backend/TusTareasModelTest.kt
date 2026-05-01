package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
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
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.repository.WorkerRepository
import com.example.tustareas.util.DateHelper
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Clase que gestiona las pruebas integración del modelo tus tareas
 */
@RunWith(AndroidJUnit4::class)
class TusTareasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Variables comunes
    private lateinit var db: TusTareasDatabase
    private lateinit var repositorio: TusTareasRepository
    private lateinit var modelo: TusTareasModel

    // Configuramos la base de datos
    @Before
    fun creacionBd() {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val aplicacion = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(contexto, TusTareasDatabase::class.java).build()
        repositorio = TusTareasRepository(db)
        modelo = TusTareasModel(aplicacion, repositorio)
    }

    // Destrucción bd temporal
    @After
    fun cerrarBd() {
        db.close()
    }

    // Prueba de limpiar tareas completa
    @Test
    fun limpiarTareasCompletas() = runTest {
        // Crear tareas
        val tareaCompleta = Tarea(
            nombre = "tarea completa",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            estado = Estado.COMPLETADA
        )
        val tareaCompletaDto = TareaDTO(tareaCompleta, emptyList())
        val tareaIncompleta = Tarea(
            nombre = "tarea incompleta",
            prioridad = Prioridad.BAJA,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            estado = Estado.EN_TIEMPO
        )
        val tareaNoCompletaDto = TareaDTO(tareaIncompleta, emptyList())

        // Añadir tareas
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaCompletaDto)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaNoCompletaDto)

        // Limpia la tarea completa
        modelo.limpiarTareasCompletas()

        // Vigilamos el live data como en estadisticas model test en su versión unitaria
        val liveData = repositorio.listarTareas.obtenerTareasFiltradas(
            Prioridad.entries.map { it }.toTypedArray(),
            Estado.entries.map { it }.toTypedArray(), "",
            OrdenarTareas.FECHA_LIMITE_ASC
        )
        liveData.observeForever { }

        // Comprueba que de las 2 taras de preuba en la bd en memoria solo queda 1
        val resultado = liveData.value
        println(resultado)
        println(resultado!!.size)
        assert(
            resultado.size == 1
        )
    }

    // Prueba marcar notificaciones como leidas
    @Test
    fun marcarNotificacionComoLeida() = runTest {
        // Creación datos bd
        val tarea = Tarea(
            nombre = "tarea",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            estado = Estado.EN_TIEMPO
        )
        val tareaDTO = TareaDTO(tarea, emptyList())
        val notificacion = Notificacion(
            titulo = "prueba",
            mensaje = "prueba",
            leido = false,
            idTarea = 1
        )
        // Añadir a la bd
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaDTO)
        WorkerRepository(db).anadirNotificacion(notificacion)

        // actualizacion de notificacion
        modelo.notificaciones(intent = Intent().apply {
            putExtra("idNotificacion", 1)
        })

        // vigilar notificaciones
        val notificaicones = WorkerRepository(db).obtenerTodasLasNotificaciones()

        // Notificaciones comprobacion
        assert(notificaicones[0].leido)
    }

}