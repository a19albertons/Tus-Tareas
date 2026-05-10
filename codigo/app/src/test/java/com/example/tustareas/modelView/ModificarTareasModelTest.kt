package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.ModificarTareasRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date


/**
 * Clase que tiene las pruebas unitarias de modificar tareas model
 */
class ModificarTareasModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val modificarTareasRepository = Mockito.mock(ModificarTareasRepository::class.java)

    // Definición modelo
    val modificarTareasModel = ModificarTareasModel(Application(), modificarTareasRepository)

    @Test
    fun insertarTarea() = runTest {
        // Definición tarea de prueba
        val tarea = Tarea(1, "Tarea 1", "Descripción de la tarea 1", Date(), Prioridad.NO_ESTABLECIDO, Date(),
            Estado.COMPLETADA, null)
        val tareaDTO = TareaDTO(tarea, emptyList())

        // Definición respuestas
        Mockito.`when`(modificarTareasRepository.insertarTareaConEtiqueta(tareaDTO))
            .thenReturn(Unit)

        // Comprobación del resultado
        modificarTareasModel.insertarTareaConEtiqueta(tareaDTO)
        Mockito.verify(modificarTareasRepository).insertarTareaConEtiqueta(tareaDTO)
    }

    @Test
    fun modificarTarea() = runTest {
        // Definición tarea de prueba
        val tarea = Tarea(1, "Tarea 1", "Descripción de la tarea 1", Date(), Prioridad.NO_ESTABLECIDO, Date(),
            Estado.COMPLETADA, null)
        val tareaDTO = TareaDTO(tarea, emptyList())

        // Definición respuestas
        Mockito.`when`(modificarTareasRepository.modificarTareaConEtiqueta(tareaDTO))
            .thenReturn(Unit)

        // Comprobación del resultado
        modificarTareasModel.modificarTareaConEtiqueta(tareaDTO)
        Mockito.verify(modificarTareasRepository).modificarTareaConEtiqueta(tareaDTO)
    }
}