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

        // fecha inicio 6 abril 2026
        val inicio = 1775426400000L

        // fecha fin 12 abril 2026
        val fin = 1775944800000L

        // Definición respuestas
        `when`(estadisticasRepositorio.obtenerCantidadTareasCompletasEntre2Fechas(inicio, fin))
            .thenReturn(MutableLiveData(5L))
        `when`(estadisticasRepositorio.obtenerCantidadTareasPendientesEntre2Fechas(inicio, fin))
            .thenReturn(MutableLiveData(10L))

        // Obtener dato del observer
        val liveData = estadisticasModel.obtenerRueda(inicio, fin)
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        println(resultado)
        assert(resultado == Pair(5L, 10L))

    }
}