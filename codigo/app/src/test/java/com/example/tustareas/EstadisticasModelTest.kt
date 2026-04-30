package com.example.tustareas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.modelView.EstadisticasModel
import com.example.tustareas.repository.EstadisticasRepository
import com.example.tustareas.repository.TusTareasRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * Clase que tiene las pruebas unitarias del submodelo de estadisticas
 */
class EstadisticasModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun probarRueda1() {
        // Definición reposito
        val repositorio = mock(TusTareasRepository::class.java)
        val estadisticasRepositorio = mock(EstadisticasRepository::class.java)
        `when`(repositorio.estadisticas).thenReturn(estadisticasRepositorio)

        // Definición modelo
        val estadisticasModel = EstadisticasModel(repositorio)

        // Definición respuestas
        `when`(estadisticasRepositorio.obtenerCantidadTareasCompletasEntre2Fechas(anyLong(), anyLong()))
            .thenReturn(MutableLiveData(10L))
        `when`(estadisticasRepositorio.obtenerCantidadTareasPendientesEntre2Fechas(anyLong(), anyLong()))
            .thenReturn(MutableLiveData(10L))

        // Obtener dato del observer
        val liveData = estadisticasModel.obtenerRueda()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado == 50f)

    }
}