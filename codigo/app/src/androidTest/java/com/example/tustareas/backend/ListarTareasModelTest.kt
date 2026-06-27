package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.ListarTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.ListarTareasRepository
import com.example.tustareas.repository.ModificarTareasRepository
import com.example.tustareas.util.DateHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
 * Clase que contiene los test de integración de listar tareas model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListarTareasModelTest {
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
    lateinit var repositorioTareasModificar: ModificarTareasRepository

    @Inject
    lateinit var repositorioEtiquetasCrear: CrearEtiquetasRepository

    @Inject
    lateinit var listarTareasRepository: ListarTareasRepository

    lateinit var listarTareasModel: ListarTareasModel

    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            ruleHilt.inject()

            // Crear modelo
            listarTareasModel = ListarTareasModel(ApplicationProvider.getApplicationContext(), listarTareasRepository)

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
                    nombre = "tareaHoy",
                    descripcion = "descripcion",
                    prioridad = Prioridad.ALTA,
                    fechaCreacion = Date(DateHelper.fechaMediaNocheUTC().time - 86400000),
                    fechaLimite = Date(diaReferencia), // Hoy
                    estado = Estado.COMPLETADA,
                )
            val etiqueta =
                Etiqueta(
                    // Id interno manual para base de pruebas
                    id = 1,
                    nombre = "etiqueta",
                )
            val tareaHoyDTO = TareaDTO(tareaHoy, listOf(etiqueta))
            // Tarea retrasada, prioridad media y retrasada
            val tareaRETRASADA =
                Tarea(
                    nombre = "tareaRetrasada",
                    prioridad = Prioridad.MEDIA,
                    fechaCreacion = DateHelper.fechaMediaNocheUTC(),
                    fechaLimite = Date(diaReferencia - 86400000), // Un día antes
                    estado = Estado.RETRASADA,
                )
            val tareaRetrasadaDTO = TareaDTO(tareaRETRASADA, emptyList())

            // Insertar tareas y etiqueta
            repositorioTareasModificar.insertarTareaConEtiqueta(tarea1DTO)
            repositorioTareasModificar.insertarTareaConEtiqueta(tarea2DTO)
            repositorioEtiquetasCrear.insertarEtiqueta(etiqueta)
            repositorioTareasModificar.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioTareasModificar.insertarTareaConEtiqueta(tareaRetrasadaDTO)
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
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 4)
    }

    @Test
    fun prioridadTodosPrimero() {
        // Obtener datos
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun prioridadTodosUltimo() {
        // Obtener datos
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test prioridad No establecido
    @Test
    fun prioridadNoEstablecidoCantidad() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.NO_ESTABLECIDO).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadNoEstablecidoPrimero() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.NO_ESTABLECIDO).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun prioridadNoEstablecidoUltimo() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.NO_ESTABLECIDO).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test prioridad Baja
    @Test
    fun prioridadBajaCantidad() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.BAJA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadBajaPrimero() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.BAJA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea2")
    }

    @Test
    fun prioridadBajaUltimo() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.BAJA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea2")
    }

    // Test prioridad Media
    @Test
    fun prioridadMediaCantidad() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.MEDIA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadMediaPrimero() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.MEDIA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaRetrasada")
    }

    @Test
    fun prioridadMediaUltimo() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.MEDIA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test prioridad Alta
    @Test
    fun prioridadAltaCantidad() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.ALTA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun prioridadAltaPrimero() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.ALTA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun prioridadAltaUltimo() {
        // Obtener datos
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.ALTA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // Test estado todos
    @Test
    fun estadoTodosCantidad() {
        // Obtener datos
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 4)
    }

    @Test
    fun estadoTodosPrimero() {
        // Obtener datos
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun estadoTodosUltimo() {
        // Obtener datos
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test estado en tiempo
    @Test
    fun estadoEnTiempoCantidad() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.EN_TIEMPO).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 2)
    }

    @Test
    fun estadoEnTiempoPrimero() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.EN_TIEMPO).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun estadoEnTiempoUltimo() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.EN_TIEMPO).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea2")
    }

    // Test estado compeltada
    @Test
    fun estadoCompletadaCantidad() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.COMPLETADA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun estadoCompletadaPrimero() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.COMPLETADA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun estadoCompletadaUltimo() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.COMPLETADA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // Test estado retrasada
    @Test
    fun estadoRetrasadaCantidad() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.RETRASADA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun estadoRetrasadaPrimero() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.RETRASADA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaRetrasada")
    }

    @Test
    fun estadoRetrasadaUltimo() {
        // Obtener datos
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.RETRASADA).toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test ordenacion fecha creacion ascendente
    @Test
    fun fechaCreacionAscendentePrimero() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_ASC)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun fechaCreacionAscendenteUltimo() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_ASC)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaRetrasada")
    }

    // Test ordenacion fecha creacion descendente
    @Test
    fun fechaCreacionDescendentePrimero() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_DES)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaRetrasada")
    }

    @Test
    fun fechaCreacionDescendenteUltimo() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_CREACION_DES)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test ordenacion fecha limite ascendente
    @Test
    fun fechaLimiteAscendentePrimero() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun fechaLimiteAscendenteUltimo() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea2")
    }

    // Test ordenacion fecha limite ascendente
    @Test
    fun fechaLimiteDescendentePrimero() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea2")
    }

    @Test
    fun fechaLimiteDescenteUltimo() {
        // Obtener datos
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test filtro probando que nombre funciona
    @Test
    fun filtroPorNombreCantidad() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("TAREA1")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun filtroPorNombrePrimero() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("TAREA1")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tarea1")
    }

    @Test
    fun filtroPorNombreUltimo() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("TAREA1")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tarea1")
    }

    // Test filtro probando que descipcion funciona
    @Test
    fun filtroPorDescripcionCantidad() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("DESCRIPCION")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun filtroPorDescripcionPrimero() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("DESCRIPCION")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun filtroPorDescripcionUltimo() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("DESCRIPCION")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // Test filtro probando que etiqueta funciona
    @Test
    fun filtroPorEtiquetaCantidad() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("ETIQUETA")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.size == 1)
    }

    @Test
    fun filtroPorEtiquetaPrimero() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("ETIQUETA")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "tareaHoy")
    }

    @Test
    fun filtroPorEtiquetaUltimo() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("ETIQUETA")
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // test completo
    @Test
    fun filtroMasCompleto() {
        // Obtener datos
        listarTareasModel.actualizarTextoListadoTareas("ETIQUETA")
        listarTareasModel.actualizarEstadoListadoTareas(listOf(Estado.COMPLETADA).toTypedArray())
        listarTareasModel.actualizarPrioridadListadoTareas(listOf(Prioridad.ALTA).toTypedArray())
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_DES)
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "tareaHoy")
    }

    // test modificar tarea (marcar como completada)
    @Test
    fun marcarTareaComoCompletada() =
        runTest {
            // Obtener datos 1
            val liveData = listarTareasModel.obtenerTareasFiltradas()
            liveData.observeForever { }

            // Obtener tarea a modificar
            val modificada = liveData.value?.get(0)

            val boolean = true
            listarTareasModel.actualizarEstadoTarea(modificada!!, boolean)

            // Obtener datos 2
            val liveData2 = listarTareasModel.obtenerTareasFiltradas()
            liveData2.observeForever { }

            // Resultado
            val resultado = liveData2.value
            assert(resultado!!.first().estado == Estado.COMPLETADA)
        }

    // test modificar tarea (marcar como no completada)
    @Test
    fun marcarTareaComoNoCompletada() =
        runTest {
            // Obtener datos 1
            val liveData = listarTareasModel.obtenerTareasFiltradas()
            liveData.observeForever { }

            // Obtener tarea a modificar
            val modificada = liveData.value?.get(2)

            val boolean = false
            listarTareasModel.actualizarEstadoTarea(modificada!!, boolean)

            // Obtener datos 2
            val liveData2 = listarTareasModel.obtenerTareasFiltradas()
            liveData2.observeForever { }

            // Resultado
            val resultado = liveData2.value
            assert(resultado!!.get(2).estado != Estado.COMPLETADA)
        }
}
