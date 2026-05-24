package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TareaDetallesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

/**
 * Clase que tiene las pruebas unitarias de modificar etiquetas model
 */
class TareaDetallesModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val tareaDetallesRepository = Mockito.mock(TareaDetallesRepository::class.java)

    // Definición modelo
    val tareaDetallesModel = TareaDetallesModel(Application(), tareaDetallesRepository)

    @Test
    fun obtenerTareaPorId() {
        // Definición tarea de prueba
        val tarea =
            Tarea(
                1,
                "Tarea 1",
                "Descripción de la tarea 1",
                Date(),
                Prioridad.NO_ESTABLECIDO,
                Date(),
                Estado.COMPLETADA,
                null,
            )
        val tareaDTO = TareaDTO(tarea, emptyList())

        // Definición respuestas
        Mockito
            .`when`(tareaDetallesRepository.obtenerTareaDTOPorID(1))
            .thenReturn(MutableLiveData(tareaDTO))

        // Obtener dato del observer
        val liveData = tareaDetallesModel.obtenerTareaDTOPorID(1)
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado != null)
        assert(resultado?.tarea?.id == 1)
        assert(resultado?.tarea?.nombre == "Tarea 1")
        assert(resultado?.tarea?.descripcion == "Descripción de la tarea 1")
    }

    @Test
    fun eliminarTarea() =
        runTest {
            // Definición tarea de prueba
            val tarea =
                Tarea(
                    1,
                    "Tarea 1",
                    "Descripción de la tarea 1",
                    Date(),
                    Prioridad.NO_ESTABLECIDO,
                    Date(),
                    Estado.COMPLETADA,
                    null,
                )
            val tareaDTO = TareaDTO(tarea, emptyList())

            // Llamada al método a probar
            tareaDetallesModel.eliminarTarea(tarea)

            Mockito.verify(tareaDetallesRepository).eliminarTarea(tarea)
        }
}
