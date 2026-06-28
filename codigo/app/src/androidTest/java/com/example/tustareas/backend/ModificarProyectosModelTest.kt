package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.R
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.EtiquetaDetallesModel
import com.example.tustareas.modelView.ModificarEtiquetasModel
import com.example.tustareas.modelView.ModificarProyectosModel
import com.example.tustareas.modelView.ModificarTareasModel
import com.example.tustareas.modelView.ProyectoDetallesModel
import com.example.tustareas.modelView.TareaDetallesModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.CrearProyectosRepository
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.EtiquetaDetallesRepository
import com.example.tustareas.repository.ModificarEtiquetasRepository
import com.example.tustareas.repository.ModificarProyectosRepository
import com.example.tustareas.repository.ModificarTareasRepository
import com.example.tustareas.repository.ProyectoDetallesRepository
import com.example.tustareas.repository.TareaDetallesRepository
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
class ModificarProyectosModelTest {
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

    @Inject
    lateinit var repositorioModificarProyectos: ModificarProyectosRepository

    lateinit var modeloModificarProyecto: ModificarProyectosModel






    private val diaReferencia = 1735689600000L

    // Unas tareas, etiquetas y proyecto para las pruebas
    val tarea1 =
        Tarea(
            id = 1,
            nombre = "tarea 1",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.COMPLETADA,
        )
    val tareaDTO1 = TareaDTO(tarea1, emptyList())
    val tarea2 =
        Tarea(
            id = 2,
            nombre = "tarea 2",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.COMPLETADA,
        )
    val tareaDTO2 = TareaDTO(tarea2, emptyList())
    val etiqueta1 =
        Etiqueta(
            id = 1,
            nombre = "etiqueta 1",
        )
    val etiqueta2 =
        Etiqueta(
            id = 2,
            nombre = "etiqueta 2",
        )
    val proyecto1 =
        Proyecto(
            id = 1,
            nombre = "proyecto 1",
            descripcion = "descripcion del proyecto 1",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia),
        )
    val proyectoDTO1 =
        ProyectoDTO(
            proyecto1,
            listOf(),
            listOf(),
        )
    val proyecto2 =
        Proyecto(
            id = 2,
            nombre = "proyecto 2",
            descripcion = "descripcion del proyecto 2",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia),
        )
    val proyectoDTO2 =
        ProyectoDTO(
            proyecto2,
            listOf(etiqueta1),
            listOf(tarea1),
        )

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Inyección de dependencias
            ruleHilt.inject()

            // Creación de modelos
            modeloModificarProyecto = ModificarProyectosModel(ApplicationProvider.getApplicationContext(), repositorioModificarProyectos)



            // Insercion
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaDTO1)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaDTO2)
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta1)
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta2)
            repositorioCrearProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO1)
            repositorioCrearProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO2)
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun modificarProyectoExistente() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloModificarProyecto.modificarProyecto(
                proyectoDTO.proyecto.nombre,
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Comprobar que se ha guardado correctamente
            assertEquals(true, modeloModificarProyecto.observarResultado().value)
            assertEquals(proyectoDTO, modeloModificarProyecto.observarProyectoDTO().value)
        }

    @Test
    fun modificarProyectoExistenteNoValido() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloModificarProyecto.modificarProyecto(
                "",
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Comprobar que se ha guardado correctamente
            assertEquals(false, modeloModificarProyecto.observarResultado().value)
            assertEquals(R.string.error_modificar_proyecto, modeloModificarProyecto.observarMensajeError().value)
        }

    @Test
    fun actualizarEtiquetasDelProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Actualizar etiquetas del proyecto
            modeloModificarProyecto.actualizarEtiquetasDelProyecto(listOf(etiqueta1, etiqueta2))

            // Comprobar que las etiquetas del proyecto se han actualizado correctamente
            assertEquals(listOf(etiqueta1, etiqueta2), modeloModificarProyecto.obtenerEtiquetasDelProyecto())
        }

    @Test
    fun actualizarTareasDelProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Actualizar tareas del proyecto
            modeloModificarProyecto.actualizarTareasDelProyecto(listOf(tarea1, tarea2))

            // Comprobar que las tareas del proyecto se han actualizado correctamente
            assertEquals(listOf(tarea1, tarea2), modeloModificarProyecto.obtenerTareasDelProyecto())
        }

    @Test
    fun establecerFechaInicioProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Establecer fecha de inicio del proyecto
            val nuevaFechaInicio = Date()
            modeloModificarProyecto.establecerFechaInicioProyecto(nuevaFechaInicio)

            // Comprobar que la fecha de inicio del proyecto se ha actualizado correctamente
            assertEquals(
                nuevaFechaInicio,
                modeloModificarProyecto
                    .observarProyectoDTO()
                    .value!!
                    .proyecto.fechaInicio
            )
        }

    @Test
    fun establecerFechaFinProyecto() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO1.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Establecer fecha de fin del proyecto
            val nuevaFechaFin = Date()
            modeloModificarProyecto.establecerFechaFinProyecto(nuevaFechaFin)

            // Comprobar que la fecha de fin del proyecto se ha actualizado correctamente
            assertEquals(nuevaFechaFin,
                modeloModificarProyecto
                    .observarProyectoDTO()
                    .value!!
                    .proyecto.fechaFin
            )
        }

    @Test
    fun anadirEtiqueta() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO2.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Observar etiquetas restantes para el proyecto
            val liveData = modeloModificarProyecto.obtenerEtiquetasRestantes()
            liveData.observeForever { }

            // acutalizar filtro etiqueta del proyecto
            modeloModificarProyecto.actualizarFiltroListaEtiquetaProyecto(proyectoDTO.etiquetas)

            // Procesa lista etiquetas resultantes
            val resultadoProcesado = modeloModificarProyecto.etiquetasRestantesProcesadas(liveData.value!!)

            // Añadir etiqueta al proyecto
            modeloModificarProyecto.anadirEtiquetaAlProyecto(resultadoProcesado.lastIndex)

            // Comprobar que la etiqueta se ha añadido correctamente al proyecto
            assertEquals(listOf(etiqueta1, etiqueta2), modeloModificarProyecto.obtenerEtiquetasDelProyecto())
        }

    @Test
    fun anadirTarea() =
        runTest {
            // Definición proyecto
            val proyectoDTO = proyectoDTO2.copy()

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Observar etiquetas restantes para el proyecto
            val liveData = modeloModificarProyecto.obtenerTareasRestantes()
            liveData.observeForever { }

            // actualizar filtro tarea del proyecto
            modeloModificarProyecto.actualizarFiltroListaTareaProyecto(proyectoDTO.tareas)

            // Procesa lista tareas resultantes
            val resultadoProcesado = modeloModificarProyecto.tareasRestantesProcesadas(liveData.value!!)

            // Añadir tarea al proyecto
            modeloModificarProyecto.anadirTareaAlProyecto(resultadoProcesado.lastIndex)

            // Comprobar que la tarea se ha añadido correctamente al proyecto
            assertEquals(listOf(tarea1, tarea2), modeloModificarProyecto.obtenerTareasDelProyecto())
        }
}
