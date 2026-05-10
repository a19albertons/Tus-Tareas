package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.InicioRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.Date
import kotlin.collections.emptyList


/**
 * Clase que tiene las pruebas unitarias de inicio model
 */
class InicioModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val inicioRepositorio = Mockito.mock(InicioRepository::class.java)

    // Definición modelo
    val inicioModel = InicioModel(Application(), inicioRepositorio)

    @Test
    fun obtenerTareasTerminanDiaEspecifico1() {
        // Definición respuestas
        Mockito.`when`(inicioRepositorio.obtenerTareasTerminanDiaEspecifico(Mockito.any()))
            .thenReturn(MutableLiveData(emptyList()))

        // Obtener dato del observer
        val liveData = inicioModel.obtenerTareasTerminanDiaEspecifico(Mockito.any())
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.isEmpty())
    }

    @Test
    fun obtenerTareasTerminanDiaEspecifico2() {
        // Definición respuestas
        Mockito.`when`(inicioRepositorio.obtenerTareasTerminanDiaEspecifico(Mockito.any()))
            .thenReturn(MutableLiveData(listOf(
                Tarea(1, "Tarea 1", "Descripción de la tarea 1", Date(), Prioridad.NO_ESTABLECIDO,
                    Date(), Estado.COMPLETADA),
                Tarea(2, "Tarea 2", "Descripción de la tarea 2", Date(), Prioridad.ALTA,
                    Date(), Estado.RETRASADA)
            )))

        // Obtener dato del observer
        val liveData = inicioModel.obtenerTareasTerminanDiaEspecifico(Mockito.any())
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.size == 2)
    }

    @Test
    fun obtenerTareasRetrasadas() {
        // Definición respuestas
        Mockito.`when`(inicioRepositorio.obtenerTareasRetrasadas())
            .thenReturn(MutableLiveData(emptyList()))

        // Obtener dato del observer
        val liveData = inicioModel.obtenerTareasRetrasadas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.isEmpty())
    }

    @Test
    fun obtenerTareasRetrasadas2() {
        // Definición respuestas
        Mockito.`when`(inicioRepositorio.obtenerTareasRetrasadas())           .thenReturn(MutableLiveData(listOf(
                Tarea(1, "Tarea 1", "Descripción de la tarea 1", Date(), Prioridad.NO_ESTABLECIDO,
                    Date(), Estado.COMPLETADA),
                Tarea(2, "Tarea 2", "Descripción de la tarea 2", Date(), Prioridad.ALTA,
                    Date(), Estado.RETRASADA)
            )))

        // Obtener dato del observer
        val liveData = inicioModel.obtenerTareasRetrasadas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.size == 2)
    }

    @Test
    fun obtenerTareasProximas() {
        // Definición respuestas
        Mockito.`when`(inicioRepositorio.obtenerTareasProximas(Mockito.any()))
            .thenReturn(MutableLiveData(emptyList()))

        // Obtener dato del observer
        val liveData = inicioModel.obtenerTareasProximas(Mockito.any())
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.isEmpty())
    }

    @Test
    fun obtenerTareasProximas2() {
        // Definición respuestas
        Mockito.`when`(inicioRepositorio.obtenerTareasProximas(Mockito.any()))           .thenReturn(MutableLiveData(listOf(
            Tarea(1, "Tarea 1", "Descripción de la tarea 1", Date(), Prioridad.NO_ESTABLECIDO,
                Date(), Estado.COMPLETADA),
            Tarea(2, "Tarea 2", "Descripción de la tarea 2", Date(), Prioridad.ALTA,
                Date(), Estado.RETRASADA),
            Tarea(3, "Tarea 3", "Descripción de la tarea 3", Date(), Prioridad.MEDIA,
                Date(), Estado.EN_TIEMPO)
        )))

        // Obtener dato del observer
        val liveData = inicioModel.obtenerTareasRetrasadas()
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado!!.size == 3)
    }
}