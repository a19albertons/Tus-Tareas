package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelView.ListarProyectosModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.repository.CrearEtiquetasRepository
import com.example.tustareas.repository.ListarProyectosRepository
import com.example.tustareas.repository.ModificarProyectosRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject

/**
 * Clase que gestiona las pruebas de integración de listar proyectos model
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListarProyectosModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase

    @Inject
    lateinit var modificarProyectosRepository: ModificarProyectosRepository

    @Inject
    lateinit var crearEtiquestasRepository: CrearEtiquetasRepository

    @Inject
    lateinit var listarProyectosRepository: ListarProyectosRepository

    lateinit var modelo: ListarProyectosModel

    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() =
        runBlocking {
            // Iniciar Hilt
            ruleHilt.inject()

            // Crear modelo
            modelo = ListarProyectosModel(ApplicationProvider.getApplicationContext(), listarProyectosRepository)

            // Un par de proyectos
            val proyecto1 =
                Proyecto(
                    nombre = "Proyecto 1",
                    fechaCreacion = Date(diaReferencia), // dia de referencia
                    fechaInicio = Date(diaReferencia), // dia de referencia
                    fechaFin = Date(diaReferencia + 86400000 * 7), // dia de referencia + 7
                )
            val proyectoDTO1 = ProyectoDTO(proyecto1, emptyList(), emptyList())
            val proyecto2 =
                Proyecto(
                    nombre = "Proyecto 2",
                    descripcion = "descripcion",
                    fechaCreacion = Date(diaReferencia), // dia de referencia
                    fechaInicio = Date(diaReferencia + 86400000), // dia de referencia + 1
                    fechaFin = Date(diaReferencia + 86400000 * 7), // dia de referencia + 7
                )
            val proyectoDTO2 = ProyectoDTO(proyecto2, emptyList(), emptyList())
            val proyecto3 =
                Proyecto(
                    nombre = "Proyecto 3",
                    fechaCreacion = Date(diaReferencia),
                    fechaInicio = Date(diaReferencia - 86400000 * 7), // dia de referencia - 7
                    fechaFin = Date(diaReferencia - 86400000 * 7), // dia de referencia - 7
                )
            val etiqueta =
                Etiqueta(
                    // Id interno manual para base de pruebas
                    id = 1,
                    nombre = "etiqueta 1",
                )
            val proyectoDTO3 = ProyectoDTO(proyecto3, listOf(etiqueta), emptyList())
            val proyecto4 =
                Proyecto(
                    nombre = "Proyecto 4",
                    fechaCreacion = Date(diaReferencia),
                    fechaInicio = Date(diaReferencia - 86400000 * 8), // dia de referencia - 7
                    fechaFin = Date(diaReferencia - 86400000 * 7), // dia de referencia + 7
                )
            val proyectoDTO4 = ProyectoDTO(proyecto4, emptyList(), emptyList())

            // Insertar proyectos
            modificarProyectosRepository.insertarProyectoConTareaYEtiqueta(proyectoDTO1)
            modificarProyectosRepository.insertarProyectoConTareaYEtiqueta(proyectoDTO2)
            crearEtiquestasRepository.insertarEtiqueta(etiqueta)
            modificarProyectosRepository.insertarProyectoConTareaYEtiqueta(proyectoDTO3)
            modificarProyectosRepository.insertarProyectoConTareaYEtiqueta(proyectoDTO4)
        }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    // Ordenar por inicio y fin
    @Test
    fun inicioYFinPrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FIN)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 1")
    }

    @Test
    fun inicioYFinUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FIN)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 4")
    }

    // Ordenar por inicio y fecha ascendente
    @Test
    fun inicioYFechaAscendentePrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 3")
    }

    @Test
    fun inicioYFechaAscendenteUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 2")
    }

    // Ordenar por inicio y fecha descendente
    @Test
    fun inicioYFechaDescendentePrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 1")
    }

    @Test
    fun inicioYFechaDescendenteUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 4")
    }

    // Ordenar por fecha ascendente y fin
    @Test
    fun fechaAscendenteYFinPrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FIN)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 4")
    }

    @Test
    fun fechaAscendenteYFinUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FIN)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 2")
    }

    // Ordenar por fecha ascendente y fecha ascendente
    @Test
    fun fechaAscendenteYFechaAscendentePrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 4")
    }

    @Test
    fun fechaAscendenteYFechaAscendenteUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 2")
    }

    // Ordenar por fecha ascendente y fecha descendente
    @Test
    fun fechaAscendenteYFechaDescendentePrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 1")
    }

    @Test
    fun fechaAscendenteYFechaDescendenteUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 3")
    }

    // Ordenar por fecha descente y fin
    @Test
    fun fechaDescendenteYFinPrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FIN)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 2")
    }

    @Test
    fun fechaDescendenteYFinUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FIN)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 4")
    }

    // Ordenar por fecha descendente y fecha ascendente
    @Test
    fun fechaDescendenteYFechaAscendentePrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 3")
    }

    @Test
    fun fechaDescendenteYFechaAscendenteUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 1")
    }

    // Ordenar por fecha descendente y fecha ascendente
    @Test
    fun fechaDescendenteYFechaDescendentePrimero() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 2")
    }

    @Test
    fun fechaDescendenteYFechaDescendenteUltima() {
        // Configuración
        modelo.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
        modelo.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "Proyecto 4")
    }

    // Filtros de nombre, descripcion y etiquetas
    @Test
    fun filtroPorNombre() {
        // Configuración
        modelo.actualizarTextoListadoProyectos("PROyecto 4")

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 4")
    }

    @Test
    fun filtroPorDescripcion() {
        // Configuración
        modelo.actualizarTextoListadoProyectos("DESCRIPCION")

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 2")
    }

    @Test
    fun filtroPorEtiqueta() {
        // Configuración
        modelo.actualizarTextoListadoProyectos("ETiquetA")

        // Obtener datos
        val liveData = modelo.obtenerProyectosFiltradas()
        liveData.observeForever { }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.first().nombre == "Proyecto 3")
    }
}
