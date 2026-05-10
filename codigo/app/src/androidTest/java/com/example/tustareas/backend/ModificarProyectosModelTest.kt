package com.example.tustareas.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
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

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val ruleHilt = HiltAndroidRule(this)

    // Variables comunes
    @Inject
    lateinit var db: TusTareasDatabase


    @Inject
    lateinit var repositorioModificarTareas : ModificarTareasRepository

    lateinit var modeloModificarTareas: ModificarTareasModel

    @Inject
    lateinit var repositorioModificarEtiquetas : ModificarEtiquetasRepository

    lateinit var modeloModificarEtiquetas: ModificarEtiquetasModel

    @Inject
    lateinit var repositorioModificarProyectos : ModificarProyectosRepository

    lateinit var modeloModificarProyecto : ModificarProyectosModel

    @Inject
    lateinit var repositorioDetallesTarea : TareaDetallesRepository

    lateinit var modeloDetallesTarea : TareaDetallesModel

    @Inject
    lateinit var repositorioDetallesEtiquetas : EtiquetaDetallesRepository

    lateinit var modeloDetallesEtiquetas : EtiquetaDetallesModel

    @Inject
    lateinit var repositorioDetallesProyecto : ProyectoDetallesRepository

    lateinit var modeloDetallesProyectos : ProyectoDetallesModel




    private val diaReferencia = 1735689600000L

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        // Inyección de dependencias
        ruleHilt.inject()

        // Creación de modelos
        modeloModificarTareas = ModificarTareasModel(ApplicationProvider.getApplicationContext(), repositorioModificarTareas)
        modeloModificarEtiquetas = ModificarEtiquetasModel(ApplicationProvider.getApplicationContext(), repositorioModificarEtiquetas)
        modeloModificarProyecto = ModificarProyectosModel(ApplicationProvider.getApplicationContext(), repositorioModificarProyectos)
        modeloDetallesTarea = TareaDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesTarea)
        modeloDetallesEtiquetas = EtiquetaDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesEtiquetas)
        modeloDetallesProyectos = ProyectoDetallesModel(ApplicationProvider.getApplicationContext(), repositorioDetallesProyecto)

        // Unas tareas y etiquetas para las pruebas
        val tarea1 = Tarea(
            nombre = "tarea 1",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.COMPLETADA
        )
        val tareaDTO1 = TareaDTO(tarea1, emptyList())
        val tarea2 = Tarea(
            nombre = "tarea 2",
            prioridad = Prioridad.NO_ESTABLECIDO,
            fechaCreacion = Date(diaReferencia),
            estado = Estado.COMPLETADA
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
        modeloModificarTareas.insertarTareaConEtiqueta(tareaDTO1)
        modeloModificarTareas.insertarTareaConEtiqueta(tareaDTO2)
        modeloModificarEtiquetas.insertarEtiqueta(etiqueta1)
        modeloModificarEtiquetas.insertarEtiqueta(etiqueta2)
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
        val tarea = modeloDetallesTarea.obtenerTareaDTOPorID(1)
        tarea.observeForever {  }
        val tareaDTO = tarea.value

        val etiqueta = modeloDetallesEtiquetas.obtenerEtiquetaPorID(1)
        etiqueta.observeForever {  }
        val etiquetaDTO = etiqueta.value

        // Crear proyecto
        val proyectoDTO = ProyectoDTO(
            proyecto,
            listOf(etiquetaDTO!!),
            listOf(tareaDTO!!.tarea)
        )

        // Insertar
        modeloModificarProyecto.insertarProyectoConTareaYEtiqueta(proyectoDTO)

        // Obtener datos
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
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
        val tarea = modeloDetallesTarea.obtenerTareaDTOPorID(1)
        tarea.observeForever {  }
        val tareaDTO = tarea.value

        val etiqueta = modeloDetallesEtiquetas.obtenerEtiquetaPorID(1)
        etiqueta.observeForever {  }
        val etiquetaDTO = etiqueta.value

        // Crear proyecto
        val proyectoDTO = ProyectoDTO(
            proyecto,
            listOf(etiquetaDTO!!),
            listOf(tareaDTO!!.tarea)
        )

        // Insertar
        modeloModificarProyecto.insertarProyectoConTareaYEtiqueta(proyectoDTO)

        // Obtener datos
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
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
        val tarea = modeloDetallesTarea.obtenerTareaDTOPorID(1)
        tarea.observeForever {  }
        val tareaDTO = tarea.value

        val etiqueta = modeloDetallesEtiquetas.obtenerEtiquetaPorID(1)
        etiqueta.observeForever {  }
        val etiquetaDTO = etiqueta.value

        // Crear proyecto
        val proyectoDTO = ProyectoDTO(
            proyecto,
            listOf(etiquetaDTO!!),
            listOf(tareaDTO!!.tarea)
        )

        // Insertar
        modeloModificarProyecto.insertarProyectoConTareaYEtiqueta(proyectoDTO)

        // Obtener datos
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
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
        modeloModificarProyecto.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    }

    // Pruebas de modificar proyecto
    @Test
    fun modificarProyectoConTareaYEtiqueta1() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyecto = modeloDetallesProyectos.obtenerProyectoPorId(1)
        proyecto.observeForever {  }

        // modificar
        proyecto.value!!.proyecto.descripcion = "modificado"
        modeloModificarProyecto.modificarProyectoConTareaYEtiqueta(proyecto.value!!)

        // Obtener datos modificados
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
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
        val proyectoInicial = modeloDetallesProyectos.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modeloModificarProyecto.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val tareasRestantes = modeloModificarProyecto.obtenerTareasRestantes(proyectoModificado.proyecto.id)
        tareasRestantes.observeForever {  }

        // Insercion una tarea
        val tareasRestantesValue = tareasRestantes.value!!
        val tareas = proyectoModificado.tareas.plus(tareasRestantesValue.first())
        proyectoModificado.tareas = tareas

        // Actualizar
        modeloModificarProyecto.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.tareas.size == 1)
    }

    // Tareas restantes para una misma
    @Test
    fun modificarProyectoConTareaYEtiqueta3() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modeloDetallesProyectos.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modeloModificarProyecto.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val tareasRestantes = modeloModificarProyecto.obtenerTareasRestantes(proyectoModificado.proyecto.id)
        tareasRestantes.observeForever {  }

        // Insercion una tarea
        val tareasRestantesValue = tareasRestantes.value!!
        val tareas = proyectoModificado.tareas.plus(tareasRestantesValue.first())
        proyectoModificado.tareas = tareas

        // Actualizar
        modeloModificarProyecto.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Cantidad de tareas restantes
        modeloModificarProyecto.actualizarFiltroListaTareaProyecto(liveData.value!!.tareas)
        val liveData2 = modeloModificarProyecto.obtenerTareasRestantes(proyectoModificado.proyecto.id)
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
        val proyectoInicial = modeloDetallesProyectos.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de etiquetas restantes
        modeloModificarProyecto.actualizarFiltroListaEtiquetaProyecto(proyectoModificado.etiquetas)
        val etiquetasRestantes = modeloModificarProyecto.obtenerEtiquetasRestantes()
        etiquetasRestantes.observeForever {  }

        // Insercion una etiqueta
        val etiquetasRestantesValue = etiquetasRestantes.value!!
        val etiquetas = proyectoModificado.etiquetas.plus(etiquetasRestantesValue.first())
        proyectoModificado.etiquetas = etiquetas

        // Actualizar
        modeloModificarProyecto.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.etiquetas.size == 1)
    }

    // Etiquetas restantes para una misma
    @Test
    fun modificarProyectoConTareaYEtiqueta5() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modeloDetallesProyectos.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de etiquetas restantes
        modeloModificarProyecto.actualizarFiltroListaEtiquetaProyecto(proyectoModificado.etiquetas)
        val etiquetasRestantes = modeloModificarProyecto.obtenerEtiquetasRestantes()
        etiquetasRestantes.observeForever {  }

        // Insercion una etiqueta
        val etiquetasRestantesValue = etiquetasRestantes.value!!
        val etiquetas = proyectoModificado.etiquetas.plus(etiquetasRestantesValue.first())
        proyectoModificado.etiquetas = etiquetas

        // Actualizar
        modeloModificarProyecto.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Obtener datos modificados
        val liveData = modeloDetallesProyectos.obtenerProyectoPorId(1)
        liveData.observeForever {  }

        // Cantidad de etiquetas restantes
        modeloModificarProyecto.actualizarFiltroListaEtiquetaProyecto(liveData.value!!.etiquetas)
        val liveData2 = modeloModificarProyecto.obtenerEtiquetasRestantes()
        liveData2.observeForever {  }

        // Resultado
        val resultado = liveData2.value
        assert(resultado!!.size == 1)
    }

    // Comprueba que la tarea ya asignada de las 2 existente en un proyecto nuevo no aparece en las tareas restantes
    @Test
    fun obtenerTareasRestantes() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modeloDetallesProyectos.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modeloModificarProyecto.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val tareasRestantes = modeloModificarProyecto.obtenerTareasRestantes(proyectoModificado.proyecto.id)
        tareasRestantes.observeForever {  }

        // Insercion una tarea
        val tareasRestantesValue = tareasRestantes.value!!
        val tareas = proyectoModificado.tareas.plus(tareasRestantesValue.first())
        proyectoModificado.tareas = tareas

        // Actualizar
        modeloModificarProyecto.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // otro proyecto
        val proyecto2 = Proyecto(
            id = 2,
            nombre = "Proyecto 2",
            descripcion = "descripcion",
            fechaCreacion = Date(diaReferencia),
            fechaInicio = Date(diaReferencia),
            fechaFin = Date(diaReferencia)
        )
        val proyectoDTO2 = ProyectoDTO(
            proyecto2,
            emptyList(),
            emptyList()
        )
        // Insertar
        modeloModificarProyecto.insertarProyectoConTareaYEtiqueta(proyectoDTO2)

        // Gestion de obtención de tareas restantes en otra proyecto inexistente (lista vacia)
        modeloModificarProyecto.actualizarFiltroListaTareaProyecto(emptyList())
        val tareasRestantesFinales = modeloModificarProyecto.obtenerTareasRestantes(proyectoDTO2.proyecto.id)
        tareasRestantesFinales.observeForever {  }

        // Resultado
        val resultado = tareasRestantesFinales.value
        assert(resultado!!.size == 1)

    }

    // Comprueba que una tarea aún asignada en base de datos, esta disponible para volver
    // a ser añadida a un proyecto tras eliminarla de la lista en memoria que hay en el fragmento
    @Test
    fun obtenerTareasRestantes2() = runTest {
        // Anadir proyecto
        anadirProyecto()

        // Obtener proyecto
        val proyectoInicial = modeloDetallesProyectos.obtenerProyectoPorId(1)
        proyectoInicial.observeForever {  }

        // modificar
        val proyectoModificado = proyectoInicial.value!!

        // Gestion de obtención de tareas restantes
        modeloModificarProyecto.actualizarFiltroListaTareaProyecto(proyectoModificado.tareas)
        val tareasRestantes = modeloModificarProyecto.obtenerTareasRestantes(proyectoModificado.proyecto.id)
        tareasRestantes.observeForever {  }

        // Insercion una tarea
        val tareasRestantesValue = tareasRestantes.value!!
        val tareas = proyectoModificado.tareas.plus(tareasRestantesValue.first())
        proyectoModificado.tareas = tareas

        // Actualizar
        modeloModificarProyecto.modificarProyectoConTareaYEtiqueta(proyectoModificado)

        // Gestion de obtención de tareas restantes en el mismo proyecto para lista vacia (eliminar la tarea de la lista en memory del fragmento)
        modeloModificarProyecto.actualizarFiltroListaTareaProyecto(emptyList())
        val tareasRestantesFinales = modeloModificarProyecto.obtenerTareasRestantes(proyectoModificado.proyecto.id)
        tareasRestantesFinales.observeForever {  }

        // Resultado
        val resultado = tareasRestantesFinales.value
        assert(resultado!!.size == 2)

    }



}