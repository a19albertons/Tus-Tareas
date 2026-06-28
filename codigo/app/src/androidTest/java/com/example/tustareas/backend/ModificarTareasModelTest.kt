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
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.repository.ListarTareasRepository
import com.example.tustareas.repository.ModificarEtiquetasRepository
import com.example.tustareas.repository.ModificarTareasRepository
import com.example.tustareas.repository.TareaDetallesRepository
import com.example.tustareas.util.DateHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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

    @Inject
    lateinit var repositorioCrearTareas: CrearTareasRepository

    @Inject
    lateinit var repositorioCrearEtiquetas: CrearEtiquetasRepository




    private val diaReferencia = 1735689600000L

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

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Iniciar Hilt
            ruleHilt.inject()

            // Crear modelos
            modeloModificarTareas = ModificarTareasModel(ApplicationProvider.getApplicationContext(), repositorioModificarTareas)



            // Insertar tareas y etiqueta
            repositorioCrearEtiquetas.insertarEtiqueta(etiqueta)
            repositorioCrearTareas.insertarTareaConEtiqueta(tarea1DTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tarea2DTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaHoyDTO)
            repositorioCrearTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)
        }

    @After
    fun cerrarBd() {
        db.clearAllTables()
        db.close()
    }

    // Guardar tarea modificada
    @Test
    fun guardarTareaConEtiquetasExistente() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Operación de guardado
            modeloModificarTareas.modificarTarea("tareaNueva", "descripcion")

            // Comprobar que se ha guardado correctamente
            assertEquals(true, modeloModificarTareas.observarResultado().value)
            assertEquals(tareaDTO, modeloModificarTareas.observarTareaDTO().value)
        }

    @Test
    fun guardarTareaConEtiquetasExistenteNoValida() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Operación de guardado
            modeloModificarTareas.modificarTarea("", "descripcion")

            // Comprobar que se ha guardado correctamente
            assertEquals(false, modeloModificarTareas.observarResultado().value)
            assertEquals(R.string.error_modificar_tarea, modeloModificarTareas.observarMensajeError().value)
        }

    @Test
    fun prioridadOrdinal() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tareaHoyDTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Comprobar que se ha guardado correctamente
            assertEquals(
                Prioridad.ALTA,
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad
            )
        }

    @Test
    fun cambiarPrioridadAlta() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a alta
            modeloModificarTareas.cambiarPrioridad(0)

            // Comprobar que se ha cambiado correctamente
            assertEquals(Prioridad.ALTA,
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad ,
            )
        }

    @Test
    fun cambiarPrioridadMedia() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a media
            modeloModificarTareas.cambiarPrioridad(1)

            // Comprobar que se ha cambiado correctamente
            assertEquals(
                Prioridad.MEDIA,
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad
            )
        }

    @Test
    fun cambiarPrioridadBaja() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a baja
            modeloModificarTareas.cambiarPrioridad(2)

            // Comprobar que se ha cambiado correctamente
            assertEquals(
                Prioridad.BAJA,
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad
            )
        }

    @Test
    fun cambiarPrioridadNoEstablecido() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a no establecido
            modeloModificarTareas.cambiarPrioridad(3)

            // Comprobar que se ha cambiado correctamente
            assertEquals(
                Prioridad.NO_ESTABLECIDO,
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad
            )
        }

    @Test
    fun cambiarPrioridadNoValida() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Cambiar la prioridad a un valor no válido (por ejemplo, -1)
            modeloModificarTareas.cambiarPrioridad(-1)

            // Comprobar que se ha cambiado correctamente a alta (valor por defecto)
            assertEquals(
                Prioridad.ALTA,
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.prioridad
            )
        }

    @Test
    fun actualizarFechaLimite() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()


            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Actualizar la fecha límite a dos días después
            val nuevaFechaLimite = Date(DateHelper.fechaMediaNocheUTC().time + 2 * 86400000) // Dos días después
            modeloModificarTareas.actualizarFechaLimite(nuevaFechaLimite)

            // Comprobar que se ha actualizado correctamente
            assertEquals(nuevaFechaLimite,
                modeloModificarTareas
                    .observarTareaDTO()
                    .value!!
                    .tarea.fechaLimite
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

        assertEquals(3, resultado.size)
        assertEquals("etiqueta1", resultado[0].nombre)
        assertEquals("etiqueta2", resultado[1].nombre)
        assertEquals("etiqueta3", resultado[2].nombre)
    }

    @Test
    fun comprobarListaEtiquetasVacia() {
        val etiquetas = emptyList<Etiqueta>()

        val resultado = modeloModificarTareas.comprobarListaEtiquetas(etiquetas)

        assertEquals(1, resultado.size)
        assertEquals(0, resultado[0].id)
    }

    @Test
    fun actualizarEtiquetasTareaYObtenerlas() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Actualizar las etiquetas de la tarea
            val nuevasEtiquetas =
                listOf(
                    etiqueta
                )
            modeloModificarTareas.actualizarEtiquetasTarea(nuevasEtiquetas)

            // Comprobar que se han actualizado correctamente las etiquetas
            val etiquetasActualizadas = modeloModificarTareas.obtenerListaEtiquetasTarea()
            assertEquals(1, etiquetasActualizadas.size)
            assertEquals("etiqueta", etiquetasActualizadas[0].nombre)
        }

    @Test
    fun actualizarFiltroListaEtiquetaTareas() =
        runTest {
            // Definir una nueva tarea
            val tareaDTO = tarea1DTO.copy()

            // Guardar la tarea
            modeloModificarTareas.definirTareaDTO(tareaDTO)

            // Actualizar el filtro de la lista de etiquetas de la tarea
            val etiquetaFiltro = listOf(etiqueta)

            // Observamos la consulta a base de datos
            val liveData = modeloModificarTareas.obtenerEtiquetasRestantes()
            liveData.observeForever {}

            modeloModificarTareas.actualizarFiltroListaEtiquetaTareas(etiquetaFiltro)

            // Comprobar que se ha actualizado correctamente el filtro y una tarea con ese id no existe en las etiquetas restantes
            assertTrue(etiquetaFiltro[0].id !in liveData.value!!.map { it.id })
        }
}
