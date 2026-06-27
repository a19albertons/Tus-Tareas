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
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.EtiquetaDetallesRepository
import com.example.tustareas.repository.ModificarEtiquetasRepository
import com.example.tustareas.repository.ModificarProyectosRepository
import com.example.tustareas.repository.ModificarTareasRepository
import com.example.tustareas.repository.ProyectoDetallesRepository
import com.example.tustareas.repository.TareaDetallesRepository
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
    lateinit var repositorioModificarTareas: ModificarTareasRepository


    lateinit var modeloModificarTareas: ModificarTareasModel

    @Inject
    lateinit var repositorioCrearEtiquetas: CrearEtiquetasRepository

    @Inject
    lateinit var repositorioModificarEtiquetas: ModificarEtiquetasRepository

    lateinit var modeloModificarEtiquetas: ModificarEtiquetasModel

    @Inject
    lateinit var repositorioModificarProyectos: ModificarProyectosRepository

    lateinit var modeloModificarProyecto: ModificarProyectosModel

    @Inject
    lateinit var repositorioDetallesTarea: TareaDetallesRepository

    lateinit var modeloDetallesTarea: TareaDetallesModel

    @Inject
    lateinit var repositorioDetallesEtiquetas: EtiquetaDetallesRepository

    lateinit var modeloDetallesEtiquetas: EtiquetaDetallesModel

    @Inject
    lateinit var repositorioDetallesProyecto: ProyectoDetallesRepository

    lateinit var modeloDetallesProyectos: ProyectoDetallesModel

    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Inyección de dependencias
            ruleHilt.inject()

            // Creación de modelos
            modeloModificarTareas = ModificarTareasModel(ApplicationProvider.getApplicationContext(), repositorioModificarTareas)
            modeloModificarEtiquetas = ModificarEtiquetasModel(ApplicationProvider.getApplicationContext(), repositorioModificarEtiquetas)
            modeloModificarProyecto = ModificarProyectosModel(ApplicationProvider.getApplicationContext(), repositorioModificarProyectos)
            modeloDetallesTarea = TareaDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesTarea)
            modeloDetallesEtiquetas = EtiquetaDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesEtiquetas)
            modeloDetallesProyectos = ProyectoDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesProyecto)

            // Unas tareas, etiquetas y proyecto para las pruebas
            val tarea1 =
                Tarea(
                    nombre = "tarea 1",
                    prioridad = Prioridad.NO_ESTABLECIDO,
                    fechaCreacion = Date(diaReferencia),
                    estado = Estado.COMPLETADA,
                )
            val tareaDTO1 = TareaDTO(tarea1, emptyList())
            val tarea2 =
                Tarea(
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
                    listOf(),
                    listOf(),
                )

            // Insercion
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaDTO1)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaDTO2)
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta1)
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta2)
            repositorioModificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO1)
            repositorioModificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO2)
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    @Test
    fun guardarYModificarProyectoNuevo() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 0,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba",
                    descripcion = "",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloModificarProyecto.guardarYModificarProyecto(
                proyectoDTO.proyecto.nombre,
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarProyecto.observarResultado().value == true)
            assert(modeloModificarProyecto.observarProyectoDTO().value == proyectoDTO)
        }

    @Test
    fun guardarYModificarProyectoNuevoNoValido() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 0,
                    nombre = "",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba",
                    descripcion = null,
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloModificarProyecto.guardarYModificarProyecto(
                "",
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarProyecto.observarResultado().value == false)
            assert(modeloModificarProyecto.observarMensajeError().value == R.string.error_guardar_proyecto)
        }

    @Test
    fun guardarYModificarProyectoExistente() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba",
                    descripcion = "",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloModificarProyecto.guardarYModificarProyecto(
                proyectoDTO.proyecto.nombre,
                proyectoDTO.proyecto.descripcion ?: "",
            )

            println(modeloModificarProyecto.observarResultado().value)

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarProyecto.observarResultado().value == true)
            assert(modeloModificarProyecto.observarProyectoDTO().value == proyectoDTO)
        }

    @Test
    fun guardarYModificarProyectoExistenteNoValido() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba",
                    descripcion = null,
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Guardar proyecto
            modeloModificarProyecto.guardarYModificarProyecto(
                "",
                proyectoDTO.proyecto.descripcion ?: "",
            )

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarProyecto.observarResultado().value == false)
            assert(modeloModificarProyecto.observarMensajeError().value == R.string.error_modificar_proyecto)
        }

    @Test
    fun tituloDialogoNueva() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 0,
                    nombre = "",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba",
                    descripcion = null,
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Comprobar que el título del diálogo es el correcto
            assert(modeloModificarProyecto.tituloDialogo() == R.string.confirmar_guardar_proyecto)
        }

    @Test
    fun tituloDialogoExistente() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba",
                    descripcion = null,
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Comprobar que el título del diálogo es el correcto
            assert(modeloModificarProyecto.tituloDialogo() == R.string.confirmar_modificar_proyecto)
        }

    @Test
    fun actualizarEtiquetasDelProyecto() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta1 =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba 1",
                    descripcion = "",
                )
            val etiqueta2 =
                Etiqueta(
                    id = 2,
                    nombre = "Etiqueta de prueba 2",
                    descripcion = "",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta1, etiqueta2),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Actualizar etiquetas del proyecto
            modeloModificarProyecto.actualizarEtiquetasDelProyecto(listOf(etiqueta1, etiqueta2))

            // Comprobar que las etiquetas del proyecto se han actualizado correctamente
            assert(modeloModificarProyecto.obtenerEtiquetasDelProyecto() == listOf(etiqueta1, etiqueta2))
        }

    @Test
    fun actualizarTareasDelProyecto() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea1 =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba 1",
                    descripcion = "Descripción de la tarea de prueba 1",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val tarea2 =
                Tarea(
                    id = 2,
                    nombre = "tarea de prueba 2",
                    descripcion = "Descripción de la tarea de prueba 2",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.MEDIA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta1 =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba 1",
                    descripcion = "",
                )
            val etiqueta2 =
                Etiqueta(
                    id = 2,
                    nombre = "Etiqueta de prueba 2",
                    descripcion = "",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta1, etiqueta2),
                    listOf(tarea1, tarea2),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Actualizar tareas del proyecto
            modeloModificarProyecto.actualizarTareasDelProyecto(listOf(tarea1, tarea2))

            // Comprobar que las tareas del proyecto se han actualizado correctamente
            assert(modeloModificarProyecto.obtenerTareasDelProyecto() == listOf(tarea1, tarea2))
        }

    @Test
    fun establecerFechaInicioProyecto() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta1 =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba 1",
                    descripcion = "",
                )
            val etiqueta2 =
                Etiqueta(
                    id = 2,
                    nombre = "Etiqueta de prueba 2",
                    descripcion = "",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta1, etiqueta2),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Establecer fecha de inicio del proyecto
            val nuevaFechaInicio = Date()
            modeloModificarProyecto.establecerFechaInicioProyecto(nuevaFechaInicio)

            // Comprobar que la fecha de inicio del proyecto se ha actualizado correctamente
            assert(
                modeloModificarProyecto
                    .observarProyectoDTO()
                    .value!!
                    .proyecto.fechaInicio == nuevaFechaInicio,
            )
        }

    @Test
    fun establecerFechaFinProyecto() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta1 =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba 1",
                    descripcion = "",
                )
            val etiqueta2 =
                Etiqueta(
                    id = 2,
                    nombre = "Etiqueta de prueba 2",
                    descripcion = "",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta1, etiqueta2),
                    listOf(tarea),
                )

            // Definir DTO de proyecto con tarea y etiqueta
            modeloModificarProyecto.definirProyectoDTO(proyectoDTO)

            // Establecer fecha de fin del proyecto
            val nuevaFechaFin = Date()
            modeloModificarProyecto.establecerFechaFinProyecto(nuevaFechaFin)

            // Comprobar que la fecha de fin del proyecto se ha actualizado correctamente
            assert(
                modeloModificarProyecto
                    .observarProyectoDTO()
                    .value!!
                    .proyecto.fechaFin == nuevaFechaFin,
            )
        }

    @Test
    fun anadirEtiqueta() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba",
                    descripcion = "Descripción de la tarea de prueba",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            val etiqueta1 =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba 1",
                    descripcion = "",
                )
            // Tiene que usar el mismo nombre que en la base de datos del @before para
            // que se añada correctamente al proyecto
            val etiqueta2 =
                Etiqueta(
                    id = 2,
                    nombre = "etiqueta 2",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta1),
                    listOf(tarea),
                )

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
            assert(modeloModificarProyecto.obtenerEtiquetasDelProyecto() == listOf(etiqueta1, etiqueta2))
        }

    @Test
    fun anadirTarea() =
        runTest {
            // Definición proyecto
            val proyecto =
                Proyecto(
                    id = 1,
                    nombre = "Proyecto de prueba",
                    descripcion = "Descripción del proyecto de prueba",
                    fechaCreacion = Date(),
                    fechaInicio = Date(),
                    fechaFin = Date(),
                )
            val tarea1 =
                Tarea(
                    id = 1,
                    nombre = "tarea de prueba 1",
                    descripcion = "Descripción de la tarea de prueba 1",
                    fechaCreacion = Date(),
                    fechaLimite = Date(),
                    prioridad = Prioridad.ALTA,
                    estado = Estado.EN_TIEMPO,
                    idProyecto = null,
                )
            // Tiene que usar el mismo nombre que en la base de datos del @before para
            // que se añada correctamente al proyecto
            val tarea2 =
                Tarea(
                    id = 2,
                    nombre = "tarea 2",
                    prioridad = Prioridad.NO_ESTABLECIDO,
                    fechaCreacion = Date(diaReferencia),
                    estado = Estado.COMPLETADA,
                )
            val etiqueta1 =
                Etiqueta(
                    id = 1,
                    nombre = "Etiqueta de prueba 1",
                    descripcion = "",
                )
            val etiqueta2 =
                Etiqueta(
                    id = 2,
                    nombre = "Etiqueta de prueba 2",
                    descripcion = "",
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta1, etiqueta2),
                    listOf(tarea1),
                )

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
            assert(modeloModificarProyecto.obtenerTareasDelProyecto() == listOf(tarea1, tarea2))
        }
}
