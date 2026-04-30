package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.util.DateHelper
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * Clase que contiene los test de integración de ver mas model
 */
@RunWith(AndroidJUnit4::class)
class VerMasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para saltarse los scope que se ejecutan en hilos secundarios
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Variables comunes
    private lateinit var db: TusTareasDatabase
    private lateinit var repositorio: TusTareasRepository
    private lateinit var modelo: TusTareasModel


    // En esta clase de pruebas no se usa
    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val aplicacion = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(contexto, TusTareasDatabase::class.java).build()
        repositorio = TusTareasRepository(db)
        modelo = TusTareasModel(aplicacion, repositorio)

        // Tarea sin fecha limite
        val tarea1 = Tarea(
            nombre = "tarea1",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = null,
            estado = Estado.EnTiempo
        )
        val tarea1DTO = TareaDTO(tarea1, emptyList())
        // Tarea con fecha limite, pero no retrasada
        val tarea2 = Tarea(
            nombre = "tarea2",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
            estado = Estado.EnTiempo
        )
        val tarea2DTO = TareaDTO(tarea2, emptyList())
        // Tarea hoy
        val tareaHoy = Tarea(
            nombre = "tareaHoy",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = DateHelper.fechaMediaNocheUTC(), // Hoy
            estado = Estado.EnTiempo
        )
        val tareaHoyDTO = TareaDTO(tareaHoy, emptyList())
        // Tarea retrasada
        val tareaRetrasada = Tarea(
            nombre = "tareaRetrasada",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time - 86400000), // Un día antes
            // Es el campo se mira.
            estado = Estado.Retrasada
        )
        val tareaRetrasadaDTO = TareaDTO(tareaRetrasada, emptyList())

        // Insertar tareas
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea1DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea2DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaHoyDTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)


    }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    // test modificar tarea (marcar como completada)
    @Test
    fun marcarTareaComoCompletada() = runTest {
        // Obtener datos 1
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Obtenemos la tarea a modificar
        val modificada = liveData.value?.get(0)

        val booleano = true
        modelo.verMas.actualizarEstado(modificada!!, booleano)

        // Obtener datos 2
        val liveData2 = modelo.listarTareas.obtenerTareasFiltradas()
        liveData2.observeForever {  }

        // Resultado
        val resultado = liveData2.value
        assert(resultado!!.first().estado == Estado.Completada)
    }

    // Tareas para hoy -- cantidad
    @Test
    fun tareaParaHoy1() {
        val liveData = modelo.verMas.obtenerTareasTerminanDiaEspecificoConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!!.size == 1)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaHoy2() {
        val liveData = modelo.verMas.obtenerTareasTerminanDiaEspecificoConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!![0].nombre == "tareaHoy")
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaHoy3() {
        modelo.verMas.actualizarTextoVerMas("TAREAHOY")
        val liveData = modelo.verMas.obtenerTareasTerminanDiaEspecificoConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!![0].nombre == "tareaHoy")
    }

    // Tareas para retrasadas -- cantidad
    @Test
    fun tareaParaRetrasadas1() {
        val liveData = modelo.verMas.obtenerTareasRetrasadasConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!!.size == 1)
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaRetrasads2() {
        val liveData = modelo.verMas.obtenerTareasRetrasadasConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![0].nombre == "tareaRetrasada")
    }

    // Tareas para hoy -- Nombre tarea
    @Test
    fun tareaParaRetrasads3() {

        modelo.verMas.actualizarTextoVerMas("TAREARETRASADA")
        val liveData = modelo.verMas.obtenerTareasRetrasadasConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![0].nombre == "tareaRetrasada")
    }

    // Tareas futuras -- cantidad
    @Test
    fun tareaParaFuturo1() {
        val liveData = modelo.verMas.obtenerTareasProximasConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación 1
        assert(resultado!!.size == 2)
    }

    // Tareas futuras -- Nombre tarea
    @Test
    fun tareaParaFuturo2() {
        val liveData = modelo.verMas.obtenerTareasProximasConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![0].nombre == "tarea1")
    }
    @Test
    fun tareaParaFuturo3() {
        val liveData = modelo.verMas.obtenerTareasProximasConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![1].nombre == "tarea2")
    }
    @Test
    fun tareaParaFuturo4() {
        modelo.verMas.actualizarTextoVerMas("TAREA2")
        val liveData = modelo.verMas.obtenerTareasProximasConFiltro()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value

        // Comprobación nombre
        assert(resultado!![0].nombre == "tarea2")
    }
}