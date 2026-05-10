package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.repository.EstadisticasRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

/**
 * Clase que tiene las pruebas unitarias de estadisticas
 */
class EstadisticasModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val estadisticasRepositorio = Mockito.mock(EstadisticasRepository::class.java)

    // Definición modelo
    val estadisticasModel = EstadisticasModel(Application(), estadisticasRepositorio)

    @Test
    fun probarRueda1() {
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

    @Test
    fun tareasCompletas() {
        // Definición respuesta
        Mockito.`when`(
            estadisticasRepositorio.obtenerCantidadTareasCompletas()
        )
            .thenReturn(MutableLiveData(15))

        // Obtener dato del observer
        val liveData = estadisticasModel.obtenerCantidadTareasCompletas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado == 15)
    }

    @Test
    fun tareasPendientes() {
        // Definición respuesta
        Mockito.`when`(
            estadisticasRepositorio.obtenerCantidadTareasPendientes()
        )
            .thenReturn(MutableLiveData(5))

        // Obtener dato del observer
        val liveData = estadisticasModel.obtenerCantidadTareasPendientes()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado == 5)
    }

    @Test
    fun tareasRetrasadas() {
        // Definición respuesta
        Mockito.`when`(
            estadisticasRepositorio.obtenerCantidadTareasRetrasadas()
        )
            .thenReturn(MutableLiveData(8))

        // Obtener dato del observer
        val liveData = estadisticasModel.obtenerCantidadTareasRetrasadas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado == 8)
    }
}