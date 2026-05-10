package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.repository.EstadisticasRepository
import com.example.tustareas.repository.TusTareasRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

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
        val estadisticasRepositorio = Mockito.mock(EstadisticasRepository::class.java)

        // Definición modelo
        val estadisticasModel = EstadisticasModel(Application(), estadisticasRepositorio)

        // Definición respuestas
        Mockito.`when`(
            estadisticasRepositorio.obtenerCantidadTareasCompletasEntre2Fechas(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong()
            )
        )
            .thenReturn(MutableLiveData(10L))
        Mockito.`when`(
            estadisticasRepositorio.obtenerCantidadTareasPendientesEntre2Fechas(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong()
            )
        )
            .thenReturn(MutableLiveData(10L))

        // Obtener dato del observer
        val liveData = estadisticasModel.obtenerRueda()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado == 50f)

    }
}