package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.R
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.ListarTareasModel
import com.example.tustareas.modelView.ModificarEtiquetasModel
import com.example.tustareas.modelView.ModificarTareasModel
import com.example.tustareas.modelView.TareaDetallesModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.ListarTareasRepository
import com.example.tustareas.repository.ModificarEtiquetasRepository
import com.example.tustareas.repository.ModificarTareasRepository
import com.example.tustareas.repository.TareaDetallesRepository
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
 * Clase que contiene los test de integración de modificar tareas model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ModificarTareasModelTest {
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
    lateinit var repositorioModificarTareas: ModificarTareasRepository

    lateinit var modeloModificarTareas: ModificarTareasModel

    @Inject
    lateinit var repositorioModificarEtiquetas: ModificarEtiquetasRepository

    lateinit var modeloModificarEtiquetas: ModificarEtiquetasModel

    @Inject
    lateinit var repositorioDetallesTareas: TareaDetallesRepository

    lateinit var modeloDetallesTarea: TareaDetallesModel

    @Inject
    lateinit var repositorioListarTareas: ListarTareasRepository

    lateinit var modeloListarTareas: ListarTareasModel

    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Iniciar Hilt
            ruleHilt.inject()

            // Crear modelos
            modeloModificarTareas = ModificarTareasModel(ApplicationProvider.getApplicationContext(), repositorioModificarTareas)
            modeloModificarEtiquetas = ModificarEtiquetasModel(ApplicationProvider.getApplicationContext(), repositorioModificarEtiquetas)
            modeloDetallesTarea = TareaDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesTareas)
            modeloListarTareas = ListarTareasModel(ApplicationProvider.getApplicationContext(), repositorioListarTareas)

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
            repositorioModificarEtiquetas.insertarEtiqueta(etiqueta)
            repositorioModificarTareas.insertarTareaConEtiqueta(tarea1DTO)
            repositorioModificarTareas.insertarTareaConEtiqueta(tarea2DTO)
            repositorioModificarTareas.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioModificarTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)
        }

    @After
    fun cerrarBd() {
        db.close()
    }

    // Guardar tarea con etiquetas nuevas
    @Test
    fun guardarTareaConEtiquetasNueva() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Operación de guardado
            modeloModificarTareas.guardarYModificarTarea("tareaNueva", "descripcion")

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarTareas.observarResultado().value == true)
            assert(modeloModificarTareas.observarTareaDTO().value == tareaDTO)
        }

    @Test
    fun guardarTareaConEtiquetasNuevaNoValida() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Operación de guardado
            modeloModificarTareas.guardarYModificarTarea("", "descripcion")

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarTareas.observarResultado().value == false)
            assert(modeloModificarTareas.observarMensajeError().value == R.string.error_guardar_tarea)
        }

    // Guardar tarea modificada
    @Test
    fun guardarTareaConEtiquetasExistente() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 8,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Operación de guardado
            modeloModificarTareas.guardarYModificarTarea("tareaNueva", "descripcion")

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarTareas.observarResultado().value == true)
            assert(modeloModificarTareas.observarTareaDTO().value == tareaDTO)
        }

    @Test
    fun guardarTareaConEtiquetasExistenteNoValida() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 8,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Operación de guardado
            modeloModificarTareas.guardarYModificarTarea("", "descripcion")

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarTareas.observarResultado().value == false)
            assert(modeloModificarTareas.observarMensajeError().value == R.string.error_modificar_tarea)
        }

    @Test
    fun tituloDialogoNueva() {
        // Definir una nueva tarea
        val tareaDTO =
            TareaDTO(
                Tarea(
                    id = 0,
                    nombre = "tareaNueva",
                    descripcion = "descripcion",
                    prioridad = Prioridad.ALTA,
                    fechaCreacion = Date(),
                    fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                    estado = Estado.EN_TIEMPO,
                ),
                listOf(),
            )

        // Guardar la tarea
        modeloModificarTareas.definirTareaDTO(tareaDTO)

        // Comprobar el título del diálogo
        assert(modeloModificarTareas.tituloDialogo() == R.string.confirmar_guardar_tarea)
    }

    @Test
    fun tituloDialogoExistente() {
        // Definir una nueva tarea
        val tareaDTO =
            TareaDTO(
                Tarea(
                    id = 8,
                    nombre = "tareaNueva",
                    descripcion = "descripcion",
                    prioridad = Prioridad.ALTA,
                    fechaCreacion = Date(),
                    fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                    estado = Estado.EN_TIEMPO,
                ),
                listOf(),
            )

        // Guardar la tarea
        modeloModificarTareas.definirTareaDTO(tareaDTO)

        // Comprobar el título del diálogo
        assert(modeloModificarTareas.tituloDialogo() == R.string.confirmar_modificado_tarea)
    }

    @Test
    fun prioridadOrdina() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Comprobar que se ha guardado correctamente
            assert(
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad == Prioridad.ALTA,
            )
        }

    @Test
    fun cambiarPrioridadAlta() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.BAJA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a alta
            modeloModificarTareas.cambiarPrioridad(0)

            // Comprobar que se ha cambiado correctamente
            assert(
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad == Prioridad.ALTA,
            )
        }

    @Test
    fun cambiarPrioridadMedia() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.BAJA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a media
            modeloModificarTareas.cambiarPrioridad(1)

            // Comprobar que se ha cambiado correctamente
            assert(
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad == Prioridad.MEDIA,
            )
        }

    @Test
    fun cambiarPrioridadBaja() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a baja
            modeloModificarTareas.cambiarPrioridad(2)

            // Comprobar que se ha cambiado correctamente
            assert(
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad == Prioridad.BAJA,
            )
        }

    @Test
    fun cambiarPrioridadNoEstablecido() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a no establecido
            modeloModificarTareas.cambiarPrioridad(3)

            // Comprobar que se ha cambiado correctamente
            assert(
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad == Prioridad.NO_ESTABLECIDO,
            )
        }

    @Test
    fun cambiarPrioridadNoValida() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a un valor no válido (por ejemplo, -1)
            modeloModificarTareas.cambiarPrioridad(-1)

            // Comprobar que se ha cambiado correctamente a alta (valor por defecto)
            assert(
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad == Prioridad.ALTA,
            )
        }

    @Test
    fun actualizarFechaLimite() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.ALTA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Actualizar la fecha límite a dos días después
            val nuevaFechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 2 * 86400000) // Dos días después
            modeloModificarTareas.actualizarFechaLimite(nuevaFechaLimite)

            // Comprobar que se ha actualizado correctamente
            assert(
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.fechaLimite == nuevaFechaLimite,
            )
        }

    @Test
    fun comprobarListaEtiquetas() {
        val etiquetas =
            listOf(
                Etiqueta(id = 1, nombre = "etiqueta1"),
                Etiqueta(id = 2, nombre = "etiqueta2"),
                Etiqueta(id = 3, nombre = "etiqueta3"),
            )

        val resultado = modeloModificarTareas.comprobarListaEtiquetas(etiquetas)

        assert(resultado.size == 3)
        assert(resultado[0].nombre == "etiqueta1")
        assert(resultado[1].nombre == "etiqueta2")
        assert(resultado[2].nombre == "etiqueta3")
    }

    @Test
    fun comprobarListaEtiquetasVacia() {
        val etiquetas = emptyList<Etiqueta>()

        val resultado = modeloModificarTareas.comprobarListaEtiquetas(etiquetas)

        assert(resultado.size == 1)
        assert(resultado[0].id == 0)
    }

    @Test
    fun prioridadOrdinal() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.MEDIA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Comprobar que se ha guardado correctamente
            assert(modeloModificarTareas.prioridadOrdinal() == Prioridad.MEDIA.ordinal)
        }

    @Test
    fun actualizarEtiquetasTareaYObtenerlas() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.MEDIA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Actualizar las etiquetas de la tarea
            val nuevasEtiquetas =
                listOf(
                    Etiqueta(id = 1, nombre = "etiqueta1"),
                    Etiqueta(id = 2, nombre = "etiqueta2"),
                )
            modeloModificarTareas.actualizarEtiquetasTarea(nuevasEtiquetas)

            // Comprobar que se han actualizado correctamente las etiquetas
            val etiquetasActualizadas = modeloModificarTareas.obtenerListaEtiquetasTarea()
            assert(etiquetasActualizadas.size == 2)
            assert(etiquetasActualizadas[0].nombre == "etiqueta1")
            assert(etiquetasActualizadas[1].nombre == "etiqueta2")
        }

    @Test
    fun actualizarFiltroListaEtiquetaTareas() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO =
                TareaDTO(
                    Tarea(
                        id = 0,
                        nombre = "tareaNueva",
                        descripcion = "descripcion",
                        prioridad = Prioridad.MEDIA,
                        fechaCreacion = Date(),
                        fechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 86400000), // Un día después
                        estado = Estado.EN_TIEMPO,
                    ),
                    listOf(),
                )

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Actualizar el filtro de la lista de etiquetas de la tarea
            val etiquetaFiltro = listOf(Etiqueta(id = 1, nombre = "etiqueta1"))

            // Observamos la consulta a base de datos
            val liveData = modeloModificarTareas.obtenerEtiquetasRestantes()
            liveData.observeForever {}

            modeloModificarTareas.actualizarFiltroListaEtiquetaTareas(etiquetaFiltro)

            // Comprobar que se ha actualizado correctamente el filtro y una tarea con ese id no existe en las etiquetas restantes
            assert(etiquetaFiltro[0].id !in liveData.value!!.map { it.id })
        }
}
