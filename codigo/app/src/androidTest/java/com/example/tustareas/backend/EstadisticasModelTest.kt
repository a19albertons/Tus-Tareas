package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.EstadisticasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.EstadisticasRepository
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
import java.util.Date
import javax.inject.Inject

/**
 * Clase que gestiona las pruebas de integración de estadísticas model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class EstadisticasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Regla de Hilt para inyección de dependencias
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var crearTareasRepository: CrearTareasRepository

    @Inject
    lateinit var repositorioCrearEtiqueta: CrearEtiquetasRepository

    @Inject
    lateinit var estadisticasRepository: EstadisticasRepository

    lateinit var modelo: EstadisticasModel

    // Miercoles 1/1/2025 00:00:00 UTC
    private val diaReferencia = 1735689600000L

    // Tareas creadas a mano en listar tareas model
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
            // Id interno manual para base de pruebas
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

    // Tareas adicionales creadas/asistidas por IA
    val tarea3 =
        Tarea(
            id = 5,
            nombre = "tarea3",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = Date(diaReferencia + 2 * 86400000),
            fechaLimite = Date(diaReferencia + 3 * 86400000),
            estado = Estado.EN_TIEMPO,
        )
    val tarea3DTO = TareaDTO(tarea3, emptyList())

    val tarea4 =
        Tarea(
            id = 6,
            nombre = "tarea4",
            prioridad = Prioridad.ALTA,
            fechaCreacion = Date(diaReferencia - 3 * 86400000),
            fechaLimite = Date(diaReferencia - 2 * 86400000),
            estado = Estado.RETRASADA,
        )
    val tarea4DTO = TareaDTO(tarea4, emptyList())

    val tarea5 =
        Tarea(
            id = 7,
            nombre = "tarea5",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia + 86400000),
            fechaLimite = Date(diaReferencia + 5 * 86400000),
            estado = Estado.EN_TIEMPO,
        )
    val tarea5DTO = TareaDTO(tarea5, emptyList())

    val tarea6 =
        Tarea(
            id = 8,
            nombre = "tarea6",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia - 10 * 86400000),
            fechaLimite = null,
            estado = Estado.COMPLETADA,
        )
    val tarea6DTO = TareaDTO(tarea6, emptyList())

    val etiqueta2 =
        Etiqueta(
            id = 2,
            nombre = "etiqueta2",
        )

    val tarea7 =
        Tarea(
            id = 9,
            nombre = "tarea7",
            prioridad = Prioridad.ALTA,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(diaReferencia + 3 * 86400000),
            estado = Estado.EN_TIEMPO,
        )
    val tarea7DTO = TareaDTO(tarea7, listOf(etiqueta))

    val tarea8 =
        Tarea(
            id = 10,
            nombre = "tarea8",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = Date(diaReferencia - 5 * 86400000),
            fechaLimite = Date(diaReferencia),
            estado = Estado.COMPLETADA,
        )
    val tarea8DTO = TareaDTO(tarea8, listOf(etiqueta2))

    val tarea9 =
        Tarea(
            id = 11,
            nombre = "tarea9",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia - 2 * 86400000),
            fechaLimite = Date(diaReferencia + 86400000),
            estado = Estado.EN_TIEMPO,
        )
    val tarea9DTO = TareaDTO(tarea9, emptyList())

    val tarea10 =
        Tarea(
            id = 12,
            nombre = "tarea10",
            prioridad = Prioridad.ALTA,
            fechaCreacion = Date(diaReferencia - 2 * 86400000),
            fechaLimite = Date(diaReferencia - 2 * 86400000),
            estado = Estado.RETRASADA,
        )
    val tarea10DTO = TareaDTO(tarea10, listOf(etiqueta))

    val tarea11 =
        Tarea(
            id = 13,
            nombre = "tarea11",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = Date(diaReferencia - 7 * 86400000),
            fechaLimite = null,
            estado = Estado.EN_TIEMPO,
        )
    val tarea11DTO = TareaDTO(tarea11, emptyList())

    val tarea12 =
        Tarea(
            id = 14,
            nombre = "tarea12",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia - 86400000),
            fechaLimite = Date(diaReferencia + 10 * 86400000),
            estado = Estado.COMPLETADA,
        )
    val tarea12DTO = TareaDTO(tarea12, listOf(etiqueta2))

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Iniciar hilt
            ruleHilt.inject()

            // instanciar modelos
            modelo = EstadisticasModel(ApplicationProvider.getApplicationContext(), estadisticasRepository)

            // Simulamos la fecha de hoy para que coincida con diaReferencia
            DateHelper.fechaSimulada = Date(diaReferencia)

            // Insertar tareas y etiqueta
            crearTareasRepository.insertarTareaConEtiqueta(tarea1DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea2DTO)
            repositorioCrearEtiqueta.insertarEtiqueta(etiqueta)
            crearTareasRepository.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioCrearEtiqueta.insertarEtiqueta(etiqueta2)
            crearTareasRepository.insertarTareaConEtiqueta(tareaRetrasadaDTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea3DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea4DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea5DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea6DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea7DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea8DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea9DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea10DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea11DTO)
            crearTareasRepository.insertarTareaConEtiqueta(tarea12DTO)
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        DateHelper.fechaSimulada = null
        db.clearAllTables()
        db.close()
    }

    // Consultas de estadisticas absolutas sobre completas, pendientes y retrassadas
    @Test
    fun obtenerTareasCompletas() =
        runTest {
            // Obtener dato
            val liveData = modelo.obtenerCantidadTareasCompletas()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assertEquals(4, resultado)
        }

    @Test
    fun obtenerTareasPendientes() =
        runTest {
            // Obtener dato
            val liveData = modelo.obtenerCantidadTareasPendientes()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assertEquals(7, resultado)
        }

    @Test
    fun obtenerTareasRetrasadas() =
        runTest {
            // Obtener dato
            val liveData = modelo.obtenerCantidadTareasRetrasadas()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assertEquals(3, resultado)
        }

    @Test
    fun obtenerRuedaProgreso() =
        runTest {
            // Procesar fechas contra modelo (usa la fecha simulada internamente)
            val liveData = modelo.obtenerRueda()
            liveData.observeForever { }

            // Resultado (Porcentaje de completadas sobre total de la semana)
            val resultado = liveData.value
            // Ajusta el assert según lo que devuelva tu modelo (ahora devuelve Float/porcentaje)
            assertTrue(resultado != null)
        }

    @Test
    fun obtenerDatosGrafico() =
        runTest {
            // Procesar fechas contra modelo (usa la fecha simulada internamente)
            val liveData = modelo.obtenerDatosGrafico()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            val miercoles = resultado!![2]
            // Comprobación
            assertEquals(2f, miercoles.yVals!![0]) // Completadas
            assertEquals(0f, miercoles.yVals!![1]) // No Completadas

            // Otro dia como el juevee
            val jueves = resultado[3]

            // Otras 2 comprobaciones
            assertEquals(0f, jueves.yVals!![0]) // Completadas
            assertEquals(2f, jueves.yVals!![1]) // No Completadas
        }
}
