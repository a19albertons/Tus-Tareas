package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.repository.ListarProyectosRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

/**
 * Clase que tiene las pruebas unitarias de listar proyectos model
 */
class ListarProyectosModelTest {
    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val listarProyectosRepositorio = Mockito.mock(ListarProyectosRepository::class.java)

    // Definición modelo
    val listarProyectosModel = ListarProyectosModel(Application(), listarProyectosRepositorio)

    @Test
    fun obtenerProyectosFiltradas() {
        // Definición respuestas
        Mockito.`when`(listarProyectosRepositorio.obtenerProyectosFiltradas("", OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FIN))
            .thenReturn(MutableLiveData(emptyList()))

        // Obtener dato del observer
        val liveData = listarProyectosModel.obtenerProyectosFiltradas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.isEmpty())
    }

    @Test
    fun obtenerEtiquetasFiltradas2() {
        // Definición respuestas
        Mockito.`when`(listarProyectosRepositorio.obtenerProyectosFiltradas("", OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FIN))
            .thenReturn(MutableLiveData(listOf(
                Proyecto(1, "Proyecto 1", "Descripción del proyecto 1", Date(), Date(), Date()),
                Proyecto(2, "Proyecto 2", "Descripción del proyecto 2", Date(), Date(), Date())
            )))

        // Obtener dato del observer
        val liveData = listarProyectosModel.obtenerProyectosFiltradas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.size == 2)
    }
}