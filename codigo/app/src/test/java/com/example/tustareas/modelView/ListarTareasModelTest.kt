package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.ListarTareasRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

/**
 * Clase que tiene las pruebas unitarias de listar tareas model
 */
class ListarTareasModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val listarTareasRepositorio = Mockito.mock(ListarTareasRepository::class.java)

    // Definición modelo
    val listarTareasModel = ListarTareasModel(Application(), listarTareasRepositorio)

    @Test
    fun obtenerTareasFiltradas() {
        // Definición respuestas
        Mockito
            .`when`(
                listarTareasRepositorio.obtenerTareasFiltradas(
                    Prioridad.entries.toTypedArray(),
                    Estado.entries.toTypedArray(),
                    "",
                    OrdenarTareas.FECHA_LIMITE_ASC,
                ),
            ).thenReturn(MutableLiveData(emptyList()))

        // Obtener dato del observer
        listarTareasModel.actualizarTextoListadoTareas("")
        listarTareasModel.actualizarPrioridadListadoTareas(Prioridad.entries.toTypedArray())
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
        listarTareasModel.actualizarEstadoListadoTareas(Estado.entries.toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.isEmpty())
    }

    @Test
    fun obtenerTareasFiltradas2() {
        // Definición respuestas
        Mockito
            .`when`(
                listarTareasRepositorio.obtenerTareasFiltradas(
                    Prioridad.entries.toTypedArray(),
                    Estado.entries.toTypedArray(),
                    "",
                    OrdenarTareas.FECHA_LIMITE_ASC,
                ),
            ).thenReturn(
                MutableLiveData(
                    listOf(
                        Tarea(1, "Tarea 1", "Descripción de la tarea 1", Date(), Prioridad.ALTA, Date(), Estado.COMPLETADA, null),
                        Tarea(2, "Tarea 2", "Descripción de la tarea 2", Date(), Prioridad.BAJA, Date(), Estado.RETRASADA, null),
                    ),
                ),
            )

        // Obtener dato del observer
        listarTareasModel.actualizarTextoListadoTareas("")
        listarTareasModel.actualizarPrioridadListadoTareas(Prioridad.entries.toTypedArray())
        listarTareasModel.actualizarTextoOrdenacionListadoTareas(OrdenarTareas.FECHA_LIMITE_ASC)
        listarTareasModel.actualizarEstadoListadoTareas(Estado.entries.toTypedArray())
        val liveData = listarTareasModel.obtenerTareasFiltradas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.size == 2)
    }
}
