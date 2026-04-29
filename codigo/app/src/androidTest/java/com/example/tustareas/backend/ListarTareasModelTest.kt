package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
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
 * Clase que contiene los test de integración de listar tareas model
 */
@RunWith(AndroidJUnit4::class)
class ListarTareasModelTest {

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


    private val diaReferencia = 1735689600000L

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
            fechaCreacion = Date(DateHelper.fechaMediaNocheUTC().time - 86400000),
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
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
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

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    // Test prioridad todos
    @Test
    fun prioridadTodosCantidad() {
        // Obtener datos
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 4)
    }

    @Test
    fun prioridadTodosPrimero() {
        // Obtener datos
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun prioridadTodosUltimo() {
        // Obtener datos
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test prioridad No establecido
    @Test
    fun prioridadNoEstablecidoCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.NoEstablecido).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadNoEstablecidoPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.NoEstablecido).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun prioridadNoEstablecidoUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.NoEstablecido).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test prioridad Baja
    @Test
    fun prioridadBajaCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Baja).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadBajaPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Baja).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea2")
    }

    @Test
    fun prioridadBajaUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Baja).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea2")
    }

    // Test prioridad Media
    @Test
    fun prioridadMediaCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Media).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadMediaPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Media).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaRetrasada")
    }

    @Test
    fun prioridadMediaUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Media).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test prioridad Alta
    @Test
    fun prioridadAltaCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Alta).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadAltaPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Alta).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun prioridadAltaUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Alta).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // Test estado todos
    @Test
    fun estadoTodosCantidad() {
        // Obtener datos
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 4)
    }

    @Test
    fun estadoTodosPrimero() {
        // Obtener datos
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun estadoTodosUltimo() {
        // Obtener datos
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test estado en tiempo
    @Test
    fun estadoEnTiempoCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.EnTiempo).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 2)
    }

    @Test
    fun estadoEnTiempoPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.EnTiempo).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun estadoEnTiempoUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.EnTiempo).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea2")
    }

    // Test estado compeltada
    @Test
    fun estadoCompletadaCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.Completada).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun estadoCompletadaPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.Completada).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun estadoCompletadaUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.Completada).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // Test estado retrasada
    @Test
    fun estadoRetrasadaCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.Retrasada).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun estadoRetrasadaPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.Retrasada).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaRetrasada")
    }

    @Test
    fun estadoRetrasadaUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.Retrasada).toTypedArray())
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test ordenacion fecha creacion ascendente
    @Test
    fun fechaCreacionAscendentePrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_ASC)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun fechaCreacionAscendenteUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_ASC)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test ordenacion fecha creacion descendente
    @Test
    fun fechaCreacionDescendentePrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_DES)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaRetrasada")
    }

    @Test
    fun fechaCreacionDescendenteUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_DES)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test ordenacion fecha limite ascendente
    @Test
    fun fechaLimiteAscendentePrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun fechaLimiteAscendenteUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea2")
    }

    // Test ordenacion fecha limite ascendente
    @Test
    fun fechaLimiteDescendentePrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea2")
    }

    @Test
    fun fechaLimiteDescenteUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test filtro probando que nombre funciona
    @Test
    fun filtroPorNombreCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("TAREA1")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun filtroPorNombrePrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("TAREA1")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun filtroPorNombreUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("TAREA1")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test filtro probando que descipcion funciona
    @Test
    fun filtroPorDescripcionCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("DESCRIPCION")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun filtroPorDescripcionPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("DESCRIPCION")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun filtroPorDescripcionUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("DESCRIPCION")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // Test filtro probando que etiqueta funciona
    @Test
    fun filtroPorEtiquetaCantidad() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("ETIQUETA")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun filtroPorEtiquetaPrimero() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("ETIQUETA")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun filtroPorEtiquetaUltimo() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("ETIQUETA")
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // test completo
    @Test
    fun filtroMasCompleto() {
        // Obtener datos
        modelo.listarTareas.actualizarTextoListadoTareas("ETIQUETA")
        modelo.listarTareas.actualizarEstadoListadoTareas(listOf(Estado.Completada).toTypedArray())
        modelo.listarTareas.actualizarPrioridadListadoTareas(listOf(Prioridad.Alta).toTypedArray())
        modelo.listarTareas.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // test modificar tarea (marcar como completada)
    @Test
    fun marcarTareaComoCompletada() = runTest {
        // Obtener datos 1
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Obtener tarea a modificar
        val modificada = liveData.value?.get(0)

        val checkBox = androidx.appcompat.widget.AppCompatCheckBox(ApplicationProvider.getApplicationContext())
        checkBox.isChecked = true
        modelo.listarTareas.clickCheckbox(modificada!!, checkBox)

        // Obtener datos 2
        val liveData2 = modelo.listarTareas.obtenerTareasFiltradas()
        liveData2.observeForever {  }

        // Resultado
        val resultado = liveData2.value
        assert(resultado!!.first().estado == Estado.Completada)
    }

    // test modificar tarea (marcar como no completada)
    @Test
    fun marcarTareaComoNoCompletada() = runTest {
        // Obtener datos 1
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Obtener tarea a modificar
        val modificada = liveData.value?.get(2)

        val checkBox = androidx.appcompat.widget.AppCompatCheckBox(ApplicationProvider.getApplicationContext())
        checkBox.isChecked = false
        modelo.listarTareas.clickCheckbox(modificada!!, checkBox)

        // Obtener datos 2
        val liveData2 = modelo.listarTareas.obtenerTareasFiltradas()
        liveData2.observeForever {  }

        // Resultado
        val resultado = liveData2.value
        assert(resultado!!.get(2).estado != Estado.Completada)
    }
}