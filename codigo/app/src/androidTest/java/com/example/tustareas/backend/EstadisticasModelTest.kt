package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
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
 * Clase que gestiona las pruebas de integración de estadísticas model
 */
@RunWith(AndroidJUnit4::class)
class EstadisticasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Variables comunes
    private lateinit var db: TusTareasDatabase
    private lateinit var repositorio: TusTareasRepository
    private lateinit var modelo: TusTareasModel



    // Miercoles 1/1/2025 00:00:00 UTC
    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        // Simulamos la fecha de hoy para que coincida con diaReferencia
        DateHelper.fechaSimulada = Date(diaReferencia)

        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val aplicacion = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(contexto, TusTareasDatabase::class.java).build()
        repositorio = TusTareasRepository(db)
        modelo = TusTareasModel(aplicacion, repositorio)

        // Tareas creadas a mano en listar tareas model
        val tarea1 = Tarea(
            id = 1,
            nombre = "tarea1",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia - 86400000),
            fechaLimite = null,
            estado = Estado.EN_TIEMPO
        )
        val tarea1DTO = TareaDTO(tarea1, emptyList())
        // Tarea con fecha limite, pero no retrasada, prioridad baja, y más vieja en creación
        val tarea2 = Tarea(
            nombre = "tarea2",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia),
            fechaLimite = Date(diaReferencia + 86400000), // Un día después
            estado = Estado.EN_TIEMPO
        )
        val tarea2DTO = TareaDTO(tarea2, emptyList())
        // Completada, priroridad alta, descripcion
        val tareaHoy = Tarea(
            nombre = "tareaHoy",
            descripcion = "descripcion",
            prioridad = Prioridad.ALTA,
            fechaCreacion = Date(DateHelper.fechaMediaNocheUTC().time - 86400000),
            fechaLimite = Date(diaReferencia), // Hoy
            estado = Estado.COMPLETADA
        )
        val etiqueta = Etiqueta(
            // Id interno manual para base de pruebas
            id = 1,
            nombre = "etiqueta"
        )
        val tareaHoyDTO = TareaDTO(tareaHoy, listOf(etiqueta))
        // Tarea retrasada, prioridad media y retrasada
        val tareaRETRASADA = Tarea(
            nombre = "tareaRetrasada",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(diaReferencia - 86400000), // Un día antes
            estado = Estado.RETRASADA
        )
        val tareaRetrasadaDTO = TareaDTO(tareaRETRASADA, emptyList())

        // Insertar tareas y etiqueta
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea1DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea2DTO)
        repositorio.modificacionEtiqueta.insertarEtiqueta(etiqueta)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaHoyDTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)

        // Tareas adicionales creadas/asistidas por IA
        val tarea3 = Tarea(
            nombre = "tarea3",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = Date(diaReferencia + 2 * 86400000),
            fechaLimite = Date(diaReferencia + 3 * 86400000),
            estado = Estado.EN_TIEMPO
        )
        val tarea3DTO = TareaDTO(tarea3, emptyList())

        val tarea4 = Tarea(
            nombre = "tarea4",
            prioridad = Prioridad.ALTA,
            fechaCreacion = Date(diaReferencia - 3 * 86400000),
            fechaLimite = Date(diaReferencia - 2 * 86400000),
            estado = Estado.RETRASADA
        )
        val tarea4DTO = TareaDTO(tarea4, emptyList())

        val tarea5 = Tarea(
            nombre = "tarea5",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia + 86400000),
            fechaLimite = Date(diaReferencia + 5 * 86400000),
            estado = Estado.EN_TIEMPO
        )
        val tarea5DTO = TareaDTO(tarea5, emptyList())

        val tarea6 = Tarea(
            nombre = "tarea6",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia - 10 * 86400000),
            fechaLimite = null,
            estado = Estado.COMPLETADA
        )
        val tarea6DTO = TareaDTO(tarea6, emptyList())

        val etiqueta2 = Etiqueta(
            id = 2,
            nombre = "etiqueta2"
        )
        repositorio.modificacionEtiqueta.insertarEtiqueta(etiqueta2)

        val tarea7 = Tarea(
            nombre = "tarea7",
            prioridad = Prioridad.ALTA,
            fechaCreacion = DateHelper.fechaMediaNocheUTC(),
            fechaLimite = Date(diaReferencia + 3 * 86400000),
            estado = Estado.EN_TIEMPO
        )
        val tarea7DTO = TareaDTO(tarea7, listOf(etiqueta))

        val tarea8 = Tarea(
            nombre = "tarea8",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = Date(diaReferencia - 5 * 86400000),
            fechaLimite = Date(diaReferencia),
            estado = Estado.COMPLETADA
        )
        val tarea8DTO = TareaDTO(tarea8, listOf(etiqueta2))

        val tarea9 = Tarea(
            nombre = "tarea9",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia - 2 * 86400000),
            fechaLimite = Date(diaReferencia + 86400000),
            estado = Estado.EN_TIEMPO
        )
        val tarea9DTO = TareaDTO(tarea9, emptyList())

        val tarea10 = Tarea(
            nombre = "tarea10",
            prioridad = Prioridad.ALTA,
            fechaCreacion = Date(diaReferencia - 2 * 86400000),
            fechaLimite = Date(diaReferencia - 2 * 86400000),
            estado = Estado.RETRASADA
        )
        val tarea10DTO = TareaDTO(tarea10, listOf(etiqueta))

        val tarea11 = Tarea(
            nombre = "tarea11",
            prioridad = Prioridad.MEDIA,
            fechaCreacion = Date(diaReferencia - 7 * 86400000),
            fechaLimite = null,
            estado = Estado.EN_TIEMPO
        )
        val tarea11DTO = TareaDTO(tarea11, emptyList())

        val tarea12 = Tarea(
            nombre = "tarea12",
            prioridad = Prioridad.BAJA,
            fechaCreacion = Date(diaReferencia - 86400000),
            fechaLimite = Date(diaReferencia + 10 * 86400000),
            estado = Estado.COMPLETADA
        )
        val tarea12DTO = TareaDTO(tarea12, listOf(etiqueta2))

        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea3DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea4DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea5DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea6DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea7DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea8DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea9DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea10DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea11DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea12DTO)

    }

    // Finalización entorno
    @After
    fun cerrarBd() {
        DateHelper.fechaSimulada = null
        db.close()
    }

    // Consultas de estadisticas absolutas sobre completas, pendientes y retrassadas
    @Test
    fun obtenerTareasCompletas() =runTest {
        // Obtener dato
        val liveData = modelo.estadisticas.obtenerCantidadTareasCompletas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado == 4)
    }

    @Test
    fun obtenerTareasPendientes() =runTest {
        // Obtener dato
        val liveData = modelo.estadisticas.obtenerCantidadTareasPendientes()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado == 7)
    }

    @Test
    fun obtenerTareasRetrasadas() =runTest{
        // Obtener dato
        val liveData = modelo.estadisticas.obtenerCantidadTareasRetrasadas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado == 3)
    }

    @Test
    fun obtenerRuedaProgreso() = runTest {
        // Procesar fechas contra modelo (usa la fecha simulada internamente)
        val liveData = modelo.estadisticas.obtenerRueda()
        liveData.observeForever {  }

        // Resultado (Porcentaje de completadas sobre total de la semana)
        val resultado = liveData.value
        // Ajusta el assert según lo que devuelva tu modelo (ahora devuelve Float/porcentaje)
        assert(resultado != null)
    }

    @Test
    fun obtenerDatosGrafico() = runTest {
        // Procesar fechas contra modelo (usa la fecha simulada internamente)
        val liveData = modelo.estadisticas.obtenerDatosGrafico()
        liveData.observeForever {  }

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