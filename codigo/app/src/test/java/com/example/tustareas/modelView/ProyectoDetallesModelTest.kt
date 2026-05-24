package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.repository.ProyectoDetallesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

/**
 * Clase que tiene las pruebas unitarias de modificar etiquetas model
 */
class ProyectoDetallesModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val proyectoDetallesRepository = Mockito.mock(ProyectoDetallesRepository::class.java)

    // Definición modelo
    val proyectoDetallesModel = ProyectoDetallesModel(Application(), proyectoDetallesRepository)

    @Test
    fun obtenerProyectoPorId() {
        // Definición proyecto de prueba
        val proyecto = Proyecto(1, "Proyecto 1", "Descripción del proyecto 1", Date(), Date(), Date())
        val proyectoDTO = ProyectoDTO(proyecto, emptyList(), emptyList())

        // Definición respuestas
        Mockito
            .`when`(proyectoDetallesRepository.obtenerProyectoPorId(1))
            .thenReturn(MutableLiveData(proyectoDTO))

        // Obtener dato del observer
        val liveData = proyectoDetallesModel.obtenerProyectoPorId(1)
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado != null)
        assert(resultado?.proyecto?.id == 1)
        assert(resultado?.proyecto?.nombre == "Proyecto 1")
        assert(resultado?.proyecto?.descripcion == "Descripción del proyecto 1")
    }

    @Test
    fun eliminarProyecto() =
        runTest {
            // Definición proyecto de prueba
            val proyecto = Proyecto(1, "Proyecto 1", "Descripción del proyecto 1", Date(), Date(), Date())
            val proyectoDTO = ProyectoDTO(proyecto, emptyList(), emptyList())

            // Llamada al método a probar
            proyectoDetallesModel.eliminarProyectoConTareaYEtiqueta(proyectoDTO)

            Mockito.verify(proyectoDetallesRepository).eliminarProyectoConTareaYEtiqueta(proyectoDTO)
        }
}
