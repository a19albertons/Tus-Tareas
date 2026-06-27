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
import com.example.tustareas.repository.EstadisticasRepository
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
    lateinit var repositorioTareasRepository: ModificarTareasRepository

    @Inject
    lateinit var repositorioCrearEtiqueta: CrearEtiquetasRepository

    @Inject
    lateinit var estadisticasRepository: EstadisticasRepository

    lateinit var modelo: EstadisticasModel

    // Miercoles 1/1/2025 00:00:00 UTC
    private val diaReferencia = 1735689600000L

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
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea1DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea2DTO)
            repositorioCrearEtiqueta.insertarEtiqueta(etiqueta)
            repositorioTareasRepository.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tareaRetrasadaDTO)

            // Tareas adicionales creadas/asistidas por IA
            val tarea3 =
                Tarea(
                    nombre = "tarea3",
                    prioridad = Prioridad.MEDIA,
                    fechaCreacion = Date(diaReferencia + 2 * 86400000),
                    fechaLimite = Date(diaReferencia + 3 * 86400000),
                    estado = Estado.EN_TIEMPO,
                )
            val tarea3DTO = TareaDTO(tarea3, emptyList())

            val tarea4 =
                Tarea(
                    nombre = "tarea4",
                    prioridad = Prioridad.ALTA,
                    fechaCreacion = Date(diaReferencia - 3 * 86400000),
                    fechaLimite = Date(diaReferencia - 2 * 86400000),
                    estado = Estado.RETRASADA,
                )
            val tarea4DTO = TareaDTO(tarea4, emptyList())

            val tarea5 =
                Tarea(
                    nombre = "tarea5",
                    prioridad = Prioridad.BAJA,
                    fechaCreacion = Date(diaReferencia + 86400000),
                    fechaLimite = Date(diaReferencia + 5 * 86400000),
                    estado = Estado.EN_TIEMPO,
                )
            val tarea5DTO = TareaDTO(tarea5, emptyList())

            val tarea6 =
                Tarea(
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
            repositorioCrearEtiqueta.insertarEtiqueta(etiqueta2)

            val tarea7 =
                Tarea(
                    nombre = "tarea7",
                    prioridad = Prioridad.ALTA,
                    fechaCreacion = DateHelper.fechaMediaNocheUTC(),
                    fechaLimite = Date(diaReferencia + 3 * 86400000),
                    estado = Estado.EN_TIEMPO,
                )
            val tarea7DTO = TareaDTO(tarea7, listOf(etiqueta))

            val tarea8 =
                Tarea(
                    nombre = "tarea8",
                    prioridad = Prioridad.MEDIA,
                    fechaCreacion = Date(diaReferencia - 5 * 86400000),
                    fechaLimite = Date(diaReferencia),
                    estado = Estado.COMPLETADA,
                )
            val tarea8DTO = TareaDTO(tarea8, listOf(etiqueta2))

            val tarea9 =
                Tarea(
                    nombre = "tarea9",
                    prioridad = Prioridad.BAJA,
                    fechaCreacion = Date(diaReferencia - 2 * 86400000),
                    fechaLimite = Date(diaReferencia + 86400000),
                    estado = Estado.EN_TIEMPO,
                )
            val tarea9DTO = TareaDTO(tarea9, emptyList())

            val tarea10 =
                Tarea(
                    nombre = "tarea10",
                    prioridad = Prioridad.ALTA,
                    fechaCreacion = Date(diaReferencia - 2 * 86400000),
                    fechaLimite = Date(diaReferencia - 2 * 86400000),
                    estado = Estado.RETRASADA,
                )
            val tarea10DTO = TareaDTO(tarea10, listOf(etiqueta))

            val tarea11 =
                Tarea(
                    nombre = "tarea11",
                    prioridad = Prioridad.MEDIA,
                    fechaCreacion = Date(diaReferencia - 7 * 86400000),
                    fechaLimite = null,
                    estado = Estado.EN_TIEMPO,
                )
            val tarea11DTO = TareaDTO(tarea11, emptyList())

            val tarea12 =
                Tarea(
                    nombre = "tarea12",
                    prioridad = Prioridad.BAJA,
                    fechaCreacion = Date(diaReferencia - 86400000),
                    fechaLimite = Date(diaReferencia + 10 * 86400000),
                    estado = Estado.COMPLETADA,
                )
            val tarea12DTO = TareaDTO(tarea12, listOf(etiqueta2))

            repositorioTareasRepository.insertarTareaConEtiqueta(tarea3DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea4DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea5DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea6DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea7DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea8DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea9DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea10DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea11DTO)
            repositorioTareasRepository.insertarTareaConEtiqueta(tarea12DTO)
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        DateHelper.fechaSimulada = null
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
            assert(resultado == 4)
        }

    @Test
    fun obtenerTareasPendientes() =
        runTest {
            // Obtener dato
            val liveData = modelo.obtenerCantidadTareasPendientes()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assert(resultado == 7)
        }

    @Test
    fun obtenerTareasRetrasadas() =
        runTest {
            // Obtener dato
            val liveData = modelo.obtenerCantidadTareasRetrasadas()
            liveData.observeForever { }

            // Resultado
            val resultado = liveData.value
            assert(resultado == 3)
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
            assert(resultado != null)
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
            assert(miercoles.yVals!![0] == 2f) // Completadas
            assert(miercoles.yVals!![1] == 0f) // No Completadas

            // Otro dia como el juevee
            val jueves = resultado[3]

            // Otras 2 comprobaciones
            assert(jueves.yVals!![0] == 0f) // Completadas
            assert(jueves.yVals!![1] == 2f) // No Completadas
        }
}
