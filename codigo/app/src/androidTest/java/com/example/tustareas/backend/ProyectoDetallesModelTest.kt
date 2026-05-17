package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelView.ModificarEtiquetasModel
import com.example.tustareas.modelView.ModificarProyectosModel
import com.example.tustareas.modelView.ModificarTareasModel
import com.example.tustareas.modelView.ProyectoDetallesModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.ModificarEtiquetasRepository
import com.example.tustareas.repository.ModificarProyectosRepository
import com.example.tustareas.repository.ModificarTareasRepository
import com.example.tustareas.repository.ProyectoDetallesRepository
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
 * Clase que gestiona las pruebas de integracióndel proyecto detalles model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProyectoDetallesModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para corutinas
    @get:Rule
    val corutinasRule = MainDispatcherRule()

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
    lateinit var repositorioModificarProyectos: ModificarProyectosRepository

    lateinit var modeloModificarProyectos: ModificarProyectosModel

    @Inject
    lateinit var repositorioDetallesProyecto: ProyectoDetallesRepository

    lateinit var modeloDetallesProyecto: ProyectoDetallesModel

    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Inyectar dependencias
            ruleHilt.inject()

            // Inicializar modelos manualmente
            modeloModificarTareas = ModificarTareasModel(ApplicationProvider.getApplicationContext(), repositorioModificarTareas)
            modeloModificarEtiquetas = ModificarEtiquetasModel(ApplicationProvider.getApplicationContext(), repositorioModificarEtiquetas)
            modeloModificarProyectos = ModificarProyectosModel(ApplicationProvider.getApplicationContext(), repositorioModificarProyectos)
            modeloDetallesProyecto = ProyectoDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesProyecto)

            // Unas tareas y etiquetas para las pruebas
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

            // Insercion
            repositorioModificarTareas.insertarTareaConEtiqueta(tareaDTO1)
            repositorioModificarTareas.insertarTareaConEtiqueta(tareaDTO2)
            repositorioModificarEtiquetas.insertarEtiqueta(etiqueta1)
            repositorioModificarEtiquetas.insertarEtiqueta(etiqueta2)

            // Crear proyecto
            val proyecto =
                Proyecto(
                    nombre = "Proyecto 1",
                    descripcion = "descripcion",
                    fechaCreacion = Date(diaReferencia),
                    fechaInicio = Date(diaReferencia),
                    fechaFin = Date(diaReferencia),
                )
            val proyectoDTO =
                ProyectoDTO(
                    proyecto,
                    listOf(etiqueta1, etiqueta2),
                    listOf(tarea1, tarea2),
                )
            // Insertar
            modeloModificarProyectos.definirProyectoDTO(proyectoDTO)
            modeloModificarProyectos.guardarYModificarProyecto(proyectoDTO.proyecto.nombre, proyectoDTO.proyecto.descripcion ?: "")
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    @Test
    fun obtenerProyectoPorId() = runTest {
        // Obtener datos
        val liveData = modeloDetallesProyecto.obtenerProyectoPorId(1)
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.proyecto.nombre == "Proyecto 1")
    }

    @Test
    fun eliminarProyectoConTareaYEtiqueta() =
        runTest {
            // Obtener datos
            val liveData = modeloDetallesProyecto.obtenerProyectoPorId(1)
            liveData.observeForever { }

            // Eliminar proyecto
            modeloDetallesProyecto.eliminarProyectoConTareaYEtiqueta(liveData.value!!)

            // Obtener datos actualizados
            val liveData2 = modeloDetallesProyecto.obtenerProyectoPorId(1)
            liveData2.observeForever { }

            // Resultado
            val resultado = liveData2.value
            assert(resultado?.proyecto == null)
        }
}
