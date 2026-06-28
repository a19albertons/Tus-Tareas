package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.R
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.CrearProyectosModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.CrearProyectosRepository
import com.example.tustareas.repository.CrearTareasRepository
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
 * Clase que gestiona las pruebas de intregración de modificar proyectos model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CrearProyectosModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para corutinas
    @get:Rule
    val ruleCoroutines = MainDispatcherRule()

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var repositorioCrearTareas: CrearTareasRepository

    @Inject
    lateinit var repositorioCrearEtiquetas: CrearEtiquetasRepository

    @Inject
    lateinit var repositorioCrearProyectos: CrearProyectosRepository

    lateinit var modeloCrearProyecto: CrearProyectosModel





    private val diaReferencia = 1735689600000L

    // valores base
    // Unas tareas, etiquetas y proyecto para las pruebas
    val tarea1Base =
        Tarea(
            id = 1,
            nombre = "tarea 1",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.COMPLETADA,
        )
    val tareaDTO1Base = TareaDTO(tarea1Base, emptyList())
    val tarea2Base =
        Tarea(
            id = 2,
            nombre = "tarea 2",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.COMPLETADA,
        )
    val tareaDTO2Base = TareaDTO(tarea2Base, emptyList())
    val etiqueta1Base =
        Etiqueta(
            id = 1,
            nombre = "etiqueta 1",
        )
    val etiqueta2Base =
        Etiqueta(
            id = 2,
            nombre = "etiqueta 2",
        )
    val proyecto1Base =
        Proyecto(
            id = 1,
            nombre = "proyecto 1",
            descripcion = "descripcion del proyecto 1",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia),
        )
    val proyectoDTO1Base =
        ProyectoDTO(
            proyecto1Base,
            listOf(),
            listOf(),
        )
    val proyecto2Base =
        Proyecto(
            id = 2,
            nombre = "proyecto 2",
            descripcion = "descripcion del proyecto 2",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia),
        )
    val proyectoDTO2Base =
        ProyectoDTO(
            proyecto2Base,
            listOf(etiqueta1Base),
            listOf(tarea1Base),
        )

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Inyección de dependencias
            ruleHilt.inject()

            // Creación de modelos
            modeloCrearProyecto = CrearProyectosModel(ApplicationProvider.getApplicationContext(), repositorioCrearProyectos)

            // Insercion
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaDTO1Base)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaDTO2Base)
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta1Base)
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta2Base)
            repositorioCrearProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO1Base)
            repositorioCrearProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO2Base)
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun crearProyectoNuevo() =
        runTest {
            // Definición proyecto
            val proyectoDTO = ProyectoDTO(
                Proyecto(
                    id = 0,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                ),
                listOf(),
                listOf(),
            )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloCrearProyecto.guardarProyecto(
                proyectoDTO.proyecto.nombre,
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Comprobar que se ha guardado correctamente
            assertEquals(true, modeloCrearProyecto.observarResultado().value)
            assertEquals(proyectoDTO, modeloCrearProyecto.observarProyectoDTO().value)
        }

    @Test
    fun crearProyectoNuevoNoValido() =
        runTest {
            // Definición proyecto
            val proyectoDTO = ProyectoDTO(
                Proyecto(
                    id = 0,
                    nombre = "",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                ),
                listOf(),
                listOf(),
            )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloCrearProyecto.guardarProyecto(
                "",
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Comprobar que se ha guardado correctamente
            assertEquals(false, modeloCrearProyecto.observarResultado().value)
            assertEquals(R.string.error_guardar_proyecto, modeloCrearProyecto.observarMensajeError().value)
        }

    @Test
    fun actualizarEtiquetasDelProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1Base.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Actualizar etiquetas del proyecto
            modeloCrearProyecto.actualizarEtiquetasDelProyecto(listOf(etiqueta1Base, etiqueta2Base))

            // Comprobar que las etiquetas del proyecto se han actualizado correctamente
            assertEquals(listOf(etiqueta1Base, etiqueta2Base), modeloCrearProyecto.obtenerEtiquetasDelProyecto())
        }

    @Test
    fun actualizarTareasDelProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1Base.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Actualizar tareas del proyecto
            modeloCrearProyecto.actualizarTareasDelProyecto(listOf(tarea1Base, tarea2Base))

            // Comprobar que las tareas del proyecto se han actualizado correctamente
            assertEquals(listOf(tarea1Base, tarea2Base), modeloCrearProyecto.obtenerTareasDelProyecto())
        }

    @Test
    fun establecerFechaInicioProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1Base.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Establecer fecha de inicio del proyecto
            val nuevaFechaInicio = Date()
            modeloCrearProyecto.establecerFechaInicioProyecto(nuevaFechaInicio)

            // Comprobar que la fecha de inicio del proyecto se ha actualizado correctamente
            assertEquals(nuevaFechaInicio, modeloCrearProyecto.observarProyectoDTO().value!!.proyecto.fechaInicio)
        }

    @Test
    fun establecerFechaFinProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1Base.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Establecer fecha de fin del proyecto
            val nuevaFechaFin = Date()
            modeloCrearProyecto.establecerFechaFinProyecto(nuevaFechaFin)

            // Comprobar que la fecha de fin del proyecto se ha actualizado correctamente
            assertEquals(nuevaFechaFin, modeloCrearProyecto.observarProyectoDTO().value!!.proyecto.fechaFin)
        }

    @Test
    fun anadirEtiqueta() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO2Base.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Observar etiquetas restantes para el proyecto
            val liveData = modeloCrearProyecto.obtenerEtiquetasRestantes()
            liveData.observeForever { }

            // acutalizar filtro etiqueta del proyecto
            modeloCrearProyecto.actualizarFiltroListaEtiquetaProyecto(proyectoDTO.etiquetas)

            // Procesa lista etiquetas resultantes
            val resultadoProcesado = modeloCrearProyecto.etiquetasRestantesProcesadas(liveData.value!!)

            // Añadir etiqueta al proyecto
            modeloCrearProyecto.anadirEtiquetaAlProyecto(resultadoProcesado.lastIndex)

            // Comprobar que la etiqueta se ha añadido correctamente al proyecto
            assertEquals( listOf(etiqueta1Base, etiqueta2Base), modeloCrearProyecto.obtenerEtiquetasDelProyecto())
        }

    @Test
    fun anadirTarea() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO2Base.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloCrearProyecto.definirProyectoDTO(proyectoDTO)

            // Observar etiquetas restantes para el proyecto
            val liveData = modeloCrearProyecto.obtenerTareasRestantes()
            liveData.observeForever { }

            // actualizar filtro tarea del proyecto
            modeloCrearProyecto.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)

            // Procesa lista tareas resultantes
            val resultadoProcesado = modeloCrearProyecto.tareasRestantesProcesadas(liveData.value!!)

            // Añadir tarea al proyecto
            modeloCrearProyecto.anadirTareaAlProyecto(resultadoProcesado.lastIndex)

            // Comprobar que la tarea se ha añadido correctamente al proyecto
            assertEquals(listOf(tarea1Base, tarea2Base), modeloCrearProyecto.obtenerTareasDelProyecto())
        }
}
