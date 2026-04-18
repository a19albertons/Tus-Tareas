package com.example.tustareas.backend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * Clase que contiene los test de integración de modificar tareas model
 */
@RunWith(AndroidJUnit4::class)
class ModificarTareasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Variables comunes
    private lateinit var db: TusTareasDatabase
    private lateinit var repositorio: TusTareasRepository
    private lateinit var modelo: TusTareasModel


    private val diaReferencia = 1735686000000

    // Preparación entorno comun
    @Before
    fun crearBd() = runBlocking {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val aplicacion = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(contexto, TusTareasDatabase::class.java).build()
        repositorio = TusTareasRepository(db)
        modelo = TusTareasModel(aplicacion, repositorio)

        // Tarea sin fecha limite, sin prioridad y en tiempo
        val tarea1 = Tarea(
            id = 1,
            nombre = "tarea1",
            prioridad = Prioridad.NoEstablecido,
            fechaCreacion = Date(diaReferencia - 86400000),
            fechaLimite = null,
            estado = Estado.EnTiempo
        )
        val tarea1DTO = TareaDTO(tarea1, emptyList())
        // Tarea con fecha limite, pero no retrasada, prioridad baja, y más vieja en creación
        val tarea2 = Tarea(
            nombre = "tarea2",
            prioridad = Prioridad.Baja,
            fechaCreacion = Date(diaReferencia),
            fechaLimite = Date(diaReferencia + 86400000), // Un día después
            estado = Estado.EnTiempo
        )
        val tarea2DTO = TareaDTO(tarea2, emptyList())
        // Completada, priroridad alta, descripcion
        val tareaHoy = Tarea(
            nombre = "tareaHoy",
            descripcion = "descripcion",
            prioridad = Prioridad.Alta,
            fechaCreacion = Date(Date().time - 86400000),
            fechaLimite = Date(diaReferencia), // Hoy
            estado = Estado.Completada
        )
        val etiqueta = Etiqueta(
            // Id interno manual para base de pruebas
            id = 1,
            nombre = "etiqueta"
        )
        val tareaHoyDTO = TareaDTO(tareaHoy, listOf(etiqueta))
        // Tarea retrasada, prioridad media y retrasada
        val tareaRetrasada = Tarea(
            nombre = "tareaRetrasada",
            prioridad = Prioridad.Media,
            fechaCreacion = Date(),
            fechaLimite = Date(diaReferencia - 86400000), // Un día antes
            estado = Estado.Retrasada
        )
        val tareaRetrasadaDTO = TareaDTO(tareaRetrasada, emptyList())

        // Insertar tareas y etiqueta
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea1DTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tarea2DTO)
        repositorio.modificacionEtiqueta.insertarEtiqueta(etiqueta)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaHoyDTO)
        repositorio.modificarTareas.insertarTareaConEtiqueta(tareaRetrasadaDTO)


    }

    @Test
    fun tareaNuevaConEtiquetas1() = runTest {
        // Prueba inserción
        val tareaNuevaCompleta = Tarea(
            0,
            "prueba",
            "descripcion",
            Date(),
            Prioridad.NoEstablecido,
            Date(),
            Estado.EnTiempo,
            null
        )
        val etiqueta1 = Etiqueta(2, "etiqueta1")
        val etiqueta2 = Etiqueta(3, "etiqueta2","descripcion")
        val tareaNuevaDTO = TareaDTO(tareaNuevaCompleta, listOf(etiqueta1, etiqueta2))

        // Insercion
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta1)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta2)
        modelo.modificarTareas.insertarTareaConEtiqueta(tareaNuevaDTO)

        // Obtener datos
        val liveData = modelo.listarTareas.obtenerTareasFiltradas()
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.last().nombre == "prueba")
    }

    // Comprobación etiquetas
    @Test
    fun tareaNuevaConEtiquetas2() = runTest {
        // Prueba inserción
        val tareaNuevaCompleta = Tarea(
            0,
            "prueba",
            "descripcion",
            Date(),
            Prioridad.NoEstablecido,
            Date(),
            Estado.EnTiempo,
            null
        )
        val etiqueta1 = Etiqueta(2, "etiqueta1")
        val etiqueta2 = Etiqueta(3, "etiqueta2","descripcion")
        val tareaNuevaDTO = TareaDTO(tareaNuevaCompleta, listOf(etiqueta1, etiqueta2))

        // Insercion
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta1)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta2)
        modelo.modificarTareas.insertarTareaConEtiqueta(tareaNuevaDTO)

        // Obtener datos
        val liveData = modelo.tareaDetalles.obtenerTareaDTOPorID(5)
        liveData.observeForever {  }

        // Resultado
        val resultado = liveData.value
        assert(resultado!!.etiquetas.size == 2)
    }

    // Modificar una tarea
    @Test
    fun modificarTareaConEtiqueta1() = runTest {
        // Obtener referencia
        val liveData = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData.observeForever {  }

        // modficar
        val tarea = liveData.value
        tarea!!.tarea.descripcion = "modificada"
        modelo.modificarTareas.modificarTareaConEtiqueta(tarea)

        // Obtener datos finales
        val liveData2 = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData2.observeForever {  }

        // Comprobacion
        val resultado = liveData2.value
        assert(resultado!!.tarea.descripcion == "modificada")
    }

    // etiquetas asociadas
    @Test
    fun modificarTareaConEtiqueta2() = runTest {
        // nuevas etiquetas
        val etiqueta1 = Etiqueta(2, "etiqueta1")
        val etiqueta2 = Etiqueta(3, "etiqueta2","descripcion")

        // insercion
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta1)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta2)

        // Obtener referencia inicial
        val liveData = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData.observeForever {  }

        // obtener valor
        val tarea = liveData.value

        // Obtener etiquetas sin usar
        modelo.modificarTareas.actualizarFiltroListaEtiquetaTareas(tarea!!.etiquetas)
        val etiquetasSinUsarPorTarea = modelo.modificarTareas.obtenerEtiquetasRestantes()
        etiquetasSinUsarPorTarea.observeForever {  }

        // adicción etiqueta
        val etiquetasRestantes = etiquetasSinUsarPorTarea.value
        val nuevaLista = tarea.etiquetas.plus(etiquetasRestantes!!.first())
        tarea.etiquetas = nuevaLista

        // Actualización
        modelo.modificarTareas.modificarTareaConEtiqueta(tarea)

        // Obtener datos finales
        val liveData2 = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData2.observeForever {  }

        // Comprobacion
        val resultado = liveData2.value
        assert(resultado!!.etiquetas.size == 1)
    }

    // Etiquetas sin usar
    @Test
    fun modificarTareaConEtiqueta3() = runTest {
        // nuevas etiquetas
        val etiqueta1 = Etiqueta(2, "etiqueta1")
        val etiqueta2 = Etiqueta(3, "etiqueta2","descripcion")

        // insercion
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta1)
        modelo.modificarEtiquetas.insertarEtiqueta(etiqueta2)

        // Obtener referencia inicial
        val liveData = modelo.tareaDetalles.obtenerTareaDTOPorID(1)
        liveData.observeForever {  }

        // obtener valor
        val tarea = liveData.value

        // Obtener etiquetas sin usar
        modelo.modificarTareas.actualizarFiltroListaEtiquetaTareas(tarea!!.etiquetas)
        val etiquetasSinUsarPorTarea = modelo.modificarTareas.obtenerEtiquetasRestantes()
        etiquetasSinUsarPorTarea.observeForever {  }

        // adicción etiqueta
        val etiquetasRestantes = etiquetasSinUsarPorTarea.value
        val nuevaLista = tarea.etiquetas.plus(etiquetasRestantes!!.first())
        tarea.etiquetas = nuevaLista

        // Actualización
        modelo.modificarTareas.modificarTareaConEtiqueta(tarea)

        // Obtener datos finales
        modelo.modificarTareas.actualizarFiltroListaEtiquetaTareas(tarea.etiquetas)
        val liveData2 = modelo.modificarTareas.obtenerEtiquetasRestantes()
        liveData2.observeForever {  }

        // Comprobacion
        val resultado = liveData2.value
        assert(resultado!!.size == 2)
    }
}