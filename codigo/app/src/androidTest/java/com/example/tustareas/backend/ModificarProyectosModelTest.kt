package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * Clase que gestiona las pruebas de intregración de modificar proyectos model
 */
@RunWith(AndroidJUnit4::class)
class ModificarProyectosModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Variables comunes
    private lateinit var db: TusTareasDatabase
    private lateinit var repositorio: TusTareasRepository
    private lateinit var modelo: TusTareasModel


    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val aplicacion = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(contexto, TusTareasDatabase::class.java).build()
        repositorio = TusTareasRepository(db)
        modelo = TusTareasModel(aplicacion, repositorio)

        // Unas tareas y etiquetas para las pruebas
        val tarea1 = Tarea(
            nombre = "tarea 1",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.Completada
        )
        val tareaDTO1 = TareaDTO(tarea1, emptyList())
        val tarea2 = Tarea(
            nombre = "tarea 2",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.Completada
        )
        val tareaDTO2 = TareaDTO(tarea2, emptyList())
        val etiqueta1 = Etiqueta(
            id = 1,
            nombre = "etiqueta 1"
        )
        val etiqueta2 = Etiqueta(
            id = 2,
            nombre = "etiqueta 2"
        )

        // Insercion
        modelo.modificarTareas.insertarTareaConEtiqueta(tareaDTO1)
        modelo.modificarTareas.insertarTareaConEtiqueta(tareaDTO2)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta1)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta2)
    }

    // Finalización entorno
    @After
    fun cerrarBd() {
        db.close()
    }

    // Comprueba guaradaco correcto de 1 proyecto
    @Test
    fun insertarProyectoConTareaYEtiqueta1() = runTest {
        // Crear proyecto
        val proyecto = Proyecto(
            nombre = "Proyecto 1",
            descripcion = "descripcion",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia)
        )
        // Obtener una tarea y etiqueta
        val tarea = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        tarea.observeForever {  }
        val tareaDTO = tarea.value

        val etiqueta = modelo.etiquetaDetalles.obtenerEtiquetaPorID(1)
        etiqueta.observeForever {  }
        val etiquetaDTO = etiqueta.value

        // Crear proyecto
        val proyectoDTO = ProyectoDTO(
            proyecto,
            listOf(etiquetaDTO!!),
            listOf(tareaDTO!!.tarea)
        )

        // Insertar
        modelo.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)

        // Obtener datos
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // resultado
        val resultado = liveData.value
        assert(resultado!!.proyecto.nombre == "Proyecto 1")
    }

    // Comprueba recuperacion correcta de 1 tarea
    @Test
    fun insertarProyectoConTareaYEtiqueta2() = runTest {
        // Crear proyecto
        val proyecto = Proyecto(
            nombre = "Proyecto 1",
            descripcion = "descripcion",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia)
        )
        // Obtener una tarea y etiqueta
        val tarea = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        tarea.observeForever {  }
        val tareaDTO = tarea.value

        val etiqueta = modelo.etiquetaDetalles.obtenerEtiquetaPorID(1)
        etiqueta.observeForever {  }
        val etiquetaDTO = etiqueta.value

        // Crear proyecto
        val proyectoDTO = ProyectoDTO(
            proyecto,
            listOf(etiquetaDTO!!),
            listOf(tareaDTO!!.tarea)
        )

        // Insertar
        modelo.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)

        // Obtener datos
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // resultado
        val resultado = liveData.value
        assert(resultado!!.tareas.first().nombre == "tarea 1")
    }
    @Test
    fun insertarProyectoConTareaYEtiqueta3() = runTest {
        // Crear proyecto
        val proyecto = Proyecto(
            nombre = "Proyecto 1",
            descripcion = "descripcion",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia)
        )
        // Obtener una tarea y etiqueta
        val tarea = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        tarea.observeForever {  }
        val tareaDTO = tarea.value

        val etiqueta = modelo.etiquetaDetalles.obtenerEtiquetaPorID(1)
        etiqueta.observeForever {  }
        val etiquetaDTO = etiqueta.value

        // Crear proyecto
        val proyectoDTO = ProyectoDTO(
            proyecto,
            listOf(etiquetaDTO!!),
            listOf(tareaDTO!!.tarea)
        )

        // Insertar
        modelo.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)

        // Obtener datos
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // resultado
        val resultado = liveData.value
        assert(resultado!!.etiquetas.first().nombre == "etiqueta 1")
    }

    // Metodo de apoyo para las siguientes pruebas
    private fun anadirProyecto() = runTest {
        // Crear proyecto
        val proyecto = Proyecto(
            nombre = "Proyecto 1",
            descripcion = "descripcion",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia)
        )
        val proyectoDTO = ProyectoDTO(
            proyecto,
            emptyList(),
            emptyList()
        )
        // Insertar
        modelo.modificarProyectos.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    }

    // Pruebas de modificar proyecto
    @Test
    fun modificarProyectoConTareaYEtiqueta1() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyecto = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        proyecto.observeForever {  }

        // modificar
        proyecto.value!!.proyecto.descripcion = "modificado"
        modelo.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyecto.value!!)

        // Obtener datos modificados
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.proyecto.descripcion == "modificado")
    }

    // Añadir una tarea
    @Test
    fun modificarProyectoConTareaYEtiqueta2() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modelo.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val tareasRestantes = modelo.modificarProyectos.obtenerTareasRestantes()
        tareasRestantes.observeForever {  }

        // Insercion una tarea
        val tareasRestantesValue = tareasRestantes.value!!
        val tareas = proyectoModificado.tareas.plus(tareasRestantesValue.first())
        proyectoModificado.tareas = tareas

        // Actualizar
        modelo.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.tareas.size == 1)
    }

    @Test
    fun modificarProyectoConTareaYEtiqueta3() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modelo.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val tareasRestantes = modelo.modificarProyectos.obtenerTareasRestantes()
        tareasRestantes.observeForever {  }

        // Insercion una tarea
        val tareasRestantesValue = tareasRestantes.value!!
        val tareas = proyectoModificado.tareas.plus(tareasRestantesValue.first())
        proyectoModificado.tareas = tareas

        // Actualizar
        modelo.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }
        modelo.modificarProyectos.actualizarFiltroListaTareaProyecto(liveData.value!!.tareas)
        val liveData2 = modelo.modificarProyectos.obtenerTareasRestantes()
        liveData2.observeForever {  }

        // Resultado
        val resultado = liveData2.value
        assert(resultado!!.size == 1)
    }

    // Añadir una etiqueta
    @Test
    fun modificarProyectoConTareaYEtiqueta4() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modelo.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val etiquetasRestantes = modelo.modificarProyectos.obtenerEtiquetasRestantes()
        etiquetasRestantes.observeForever {  }

        // Insercion una tarea
        val etiquetasRestantesValue = etiquetasRestantes.value!!
        val etiquetas = proyectoModificado.etiquetas.plus(etiquetasRestantesValue.first())
        proyectoModificado.etiquetas = etiquetas

        // Actualizar
        modelo.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.etiquetas.size == 1)
    }

    @Test
    fun modificarProyectoConTareaYEtiqueta5() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modelo.modificarProyectos.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val etiquetasRestantes = modelo.modificarProyectos.obtenerEtiquetasRestantes()
        etiquetasRestantes.observeForever {  }

        // Insercion una tarea
        val etiquetasRestantesValue = etiquetasRestantes.value!!
        val etiquetas = proyectoModificado.etiquetas.plus(etiquetasRestantesValue.first())
        proyectoModificado.etiquetas = etiquetas

        // Actualizar
        modelo.modificarProyectos.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modelo.proyectoDetalles.obtenerProyectoPorId(1)
        liveData.observeForever {  }
        modelo.modificarProyectos.actualizarFiltroListaEtiquetaProyecto(liveData.value!!.etiquetas)
        val liveData2 = modelo.modificarProyectos.obtenerEtiquetasRestantes()
        liveData2.observeForever {  }

        // Resultado
        val resultado = liveData2.value
        assert(resultado!!.size == 1)
    }

}