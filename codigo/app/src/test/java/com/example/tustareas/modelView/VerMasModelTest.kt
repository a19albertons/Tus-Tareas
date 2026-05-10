package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tustareas.repository.VerMasRepository
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


/**
 * Clase que tiene las pruebas unitarias de modificar etiquetas model
 */
class VerMasModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val verMasRepository = Mockito.mock(VerMasRepository::class.java)

    // Definición modelo
    val verMasModel = VerMasModel(Application(), verMasRepository)

    @Test
    fun obtenerTareasTerminanDiaEspecificoConFiltro() {
        // Llamada al método a probar
        verMasModel.obtenerTareasTerminanDiaEspecificoConFiltro()

        // Comprobación del resultado
        Mockito.verify(verMasRepository).obtenerTareasTerminanDiaEspecificoConFiltro("")
    }

    @Test
    fun obtenerTareasRetrasadasConFiltro() {
        // Llamada al método a probar
        verMasModel.obtenerTareasRetrasadasConFiltro()

        // Comprobación del resultado
        Mockito.verify(verMasRepository).obtenerTareasRetrasadasConFiltro("")
    }

    @Test
    fun obtenerTareasProximasConFiltro() {
        // Llamada al método a probar
        verMasModel.obtenerTareasProximasConFiltro()

        // Comprobación del resultado
        Mockito.verify(verMasRepository).obtenerTareasProximasConFiltro("")
    }
}