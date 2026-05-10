package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.repository.ModificarProyectosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

/**
 * Clase que tiene las pruebas unitarias de modificar proyectos model
 */
class ModificarProyectosModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val modificarProyectosRepository = Mockito.mock(ModificarProyectosRepository::class.java)

    // Definición modelo
    val modificarProyectosModel = ModificarProyectosModel(Application(), modificarProyectosRepository)

    @Test
    fun insertarProyecto() = runTest {
         // Definición proyecto de prueba
        val proyecto = Proyecto(1, "Proyecto 1", "Descripción del proyecto 1", Date(), Date(), Date())
        val proyectoDTO = ProyectoDTO(proyecto, emptyList(), emptyList())

        // Definición respuestas
        Mockito.`when`(modificarProyectosRepository.insertarProyectoConTareaYEtiqueta(proyectoDTO))
            .thenReturn(Unit)

        // Comprobación del resultado
        modificarProyectosModel.insertarProyectoConTareaYEtiqueta(proyectoDTO)
        Mockito.verify(modificarProyectosRepository).insertarProyectoConTareaYEtiqueta(proyectoDTO)
    }

    @Test
    fun modificarProyecto() = runTest {
        // Definición proyecto de prueba
        val proyecto = Proyecto(1, "Proyecto 1", "Descripción del proyecto 1", Date(), Date(), Date())
        val proyectoDTO = ProyectoDTO(proyecto, emptyList(), emptyList())

        // Definición respuestas
        Mockito.`when`(modificarProyectosRepository.modificarProyectoConTareaYEtiqueta(proyectoDTO))
            .thenReturn(Unit)

        // Comprobación del resultado
        modificarProyectosModel.modificarProyectoConTareaYEtiqueta(proyectoDTO)
        Mockito.verify(modificarProyectosRepository).modificarProyectoConTareaYEtiqueta(proyectoDTO)
    }
}