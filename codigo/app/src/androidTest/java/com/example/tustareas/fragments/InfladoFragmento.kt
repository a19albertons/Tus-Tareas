package com.example.tustareas.fragments

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.fragmentos.AjustesFragment
import com.example.tustareas.fragmentos.CrearEtiquetaFragment
import com.example.tustareas.fragmentos.CrearProyectoFragment
import com.example.tustareas.fragmentos.CrearTareasFragment
import com.example.tustareas.fragmentos.EstadisticasFragment
import com.example.tustareas.fragmentos.EtiquetaDetallesFragment
import com.example.tustareas.fragmentos.InicioFragment
import com.example.tustareas.fragmentos.ListarEtiquetasFragment
import com.example.tustareas.fragmentos.ListarProyectosFragment
import com.example.tustareas.fragmentos.ListarTareasFragment
import com.example.tustareas.fragmentos.ModificarEtiquetaFragment
import com.example.tustareas.fragmentos.ModificarProyectoFragment
import com.example.tustareas.fragmentos.ModificarTareasFragment
import com.example.tustareas.fragmentos.ProyectoDetallesFragment
import com.example.tustareas.fragmentos.TareaDetallesFragment
import com.example.tustareas.fragmentos.VerMasFragment
import com.example.tustareas.helper.MainDispatcherRule
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.CrearProyectosRepository
import com.example.tustareas.repository.CrearTareasRepository
import com.example.tustareas.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * Clase que comprueba que cada fragmento se infla correctamente
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class InfladoFragmento {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val ruleInstant = InstantTaskExecutorRule()

    // Necesario para las corutinas
    @get:Rule
    val ruleCoroutines = MainDispatcherRule()

    // Necesario para hilt
    @get:Rule
    val rule = HiltAndroidRule(this)

    // escenario
    private var scenario: ActivityScenario<*>? = null

    val etiqueta = Etiqueta(id = 1, nombre = "Etiqueta 1", descripcion = "Descripción de la etiqueta 1")
    val tarea = Tarea(id = 1, nombre = "prueba", prioridad = Prioridad.NO_ESTABLECIDO, estado = Estado.COMPLETADA, fechaCreacion = Date())
    val proyecto = Proyecto(id = 1, nombre = "prueba", fechaCreacion = Date())
    val tareaDTO = TareaDTO(tarea, listOf(etiqueta))
    val proyectoDTO = ProyectoDTO(proyecto, listOf(etiqueta), listOf(tarea))

    val fragmentArgsProyectoDTO = Bundle().apply {
        putParcelable("proyectoDTO", proyectoDTO)
    }
    val fragmentArgsTareaDTO = Bundle().apply {
        putParcelable("tareaDTO", tareaDTO)
    }
    val fragmentArgsEtiqueta = Bundle().apply {
        putParcelable("etiqueta", etiqueta)
    }
    val fragmentArgsProyectoId = Bundle().apply {
        putInt("id", 1)
    }
    val fragmentArgsTareaId = Bundle().apply {
        putInt("id", 1)
    }
    val fragmentArgsEtiquetaId = Bundle().apply {
        putInt("id", 1)
    }
    val fragmentArgsVerMas = Bundle().apply {
        putInt("numeroVerMas", 1)
    }

    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var repositorioProyecto : CrearProyectosRepository

    @Inject
    lateinit var repositorioTarea : CrearTareasRepository

    @Inject
    lateinit var repositorioEtiqueta : CrearEtiquetasRepository




    // Prepara el entorno
    @Before
    fun before()
    = runBlocking {
        rule.inject()

        repositorioEtiqueta.insertarEtiqueta(etiqueta)
        repositorioTarea.insertarTareaConEtiqueta(tareaDTO)
        repositorioProyecto.insertarProyectoConTareaYEtiqueta(proyectoDTO)


    }

    // Finalización entorno
    @After
    fun cerrarBd() {
        scenario?.close()
        db.clearAllTables()
        db.close()
    }

    // Comprueba el fragmento de ajustes
    @Test
    fun inflarAjustesFragment() {
        scenario = launchFragmentInHiltContainer<AjustesFragment> {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de crear etiqueta
    @Test
    fun inflarCrearEtiquetaFragment() {
        scenario = launchFragmentInHiltContainer<CrearEtiquetaFragment>(fragmentArgs = fragmentArgsEtiqueta) {
            assert(view != null)
        }
    }


    // Comprueba el fragmento de crear proyecto
    @Test
    fun inflarCrearProyectoFragment() {
        scenario = launchFragmentInHiltContainer<CrearProyectoFragment>(fragmentArgs = fragmentArgsProyectoDTO) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de crear tarea
    @Test
    fun inflarCrearTareaFragment() {
        scenario = launchFragmentInHiltContainer<CrearTareasFragment>(fragmentArgs = fragmentArgsTareaDTO) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de estadisticas
    @Test
    fun inflarEstadisticasFragment() {
        scenario = launchFragmentInHiltContainer<EstadisticasFragment> {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de detalles de etiqueta
    @Test
    fun inflarEtiquetaDetallesFragment() {
        scenario = launchFragmentInHiltContainer<EtiquetaDetallesFragment>(fragmentArgs = fragmentArgsEtiquetaId) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de inicio
    @Test
    fun inflarInicioFragment() {
        scenario = launchFragmentInHiltContainer<InicioFragment> {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de listar etiqueta
    @Test
    fun inflarListarEtiquetasFragment() {
        scenario = launchFragmentInHiltContainer<ListarEtiquetasFragment> {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de listar proyecto
    @Test
    fun inflarListarProyectoFragment() {
        scenario = launchFragmentInHiltContainer<ListarProyectosFragment> {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de listar tarea
    @Test
    fun inflarListarTareasFragment() {
        scenario = launchFragmentInHiltContainer<ListarTareasFragment> {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de modificar etiqueta
    @Test
    fun inflarModificarEtiquetaFragment() {
        scenario = launchFragmentInHiltContainer<ModificarEtiquetaFragment>(fragmentArgs = fragmentArgsEtiqueta) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de modificar proyecto
    @Test
    fun inflarModificarProyectoFragment() {
        scenario = launchFragmentInHiltContainer<ModificarProyectoFragment>(fragmentArgs = fragmentArgsProyectoDTO) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de modificar tarea
    @Test
    fun inflarModificarTareaFragment() {
        scenario = launchFragmentInHiltContainer<ModificarTareasFragment>(fragmentArgs = fragmentArgsTareaDTO) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de detalles de proyecto
    @Test
    fun inflarProyectoDetallesFragment() {
        scenario = launchFragmentInHiltContainer<ProyectoDetallesFragment>(fragmentArgs = fragmentArgsProyectoId) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de detalles de tarea
    @Test
    fun inflarTareaDetallesFragment() {
        scenario = launchFragmentInHiltContainer<TareaDetallesFragment>(fragmentArgs = fragmentArgsTareaId) {
            assert(view != null)
        }
    }

    // Comprueba el fragmento de ver mas
    @Test
    fun inflarVerMasFragment() {
        scenario = launchFragmentInHiltContainer<VerMasFragment>(fragmentArgs = fragmentArgsVerMas) {
            assert(view != null)
        }
    }


}