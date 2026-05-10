package com.example.tustareas.modelView

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.EtiquetaDetallesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

/**
 * Clase que tiene las pruebas unitarias de etiqueta detalles
 */
class EtiquetaDetallesModelTest {

    // Necesario para saltarle el suspend que se ejecuta en segundo plano
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Definición repositorio
    val etiquetaDetallesRepositorio = Mockito.mock(EtiquetaDetallesRepository::class.java)

    // Definición modelo
    val etiquetaDetallesModel = EtiquetaDetallesModel(Application(), etiquetaDetallesRepositorio)


    @Test
    fun obtenerEtiquetaPorID() {
        // Definición respuestas
        Mockito.`when`(etiquetaDetallesRepositorio.obtenerEtiquetaPorID(1))
            .thenReturn(MutableLiveData(Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")))

        // Obtener dato del observer
        val liveData = etiquetaDetallesModel.obtenerEtiquetaPorID(1)
        liveData.observeForever {}

        // Comprobación del resultado
        val resultado = liveData.value
        assert(resultado != null)
        assert(resultado?.id == 1)
        assert(resultado?.nombre == "Etiqueta 1")
        assert(resultado?.descripcion == "Descripción de la etiqueta 1")
    }

    @Test
    fun eliminarEtiqueta() = runTest {
        // Definición etiqueta a eliminar
        val etiquetaAEliminar = Etiqueta(1, "Etiqueta 1", "Descripción de la etiqueta 1")

        // Llamada al método a probar
        etiquetaDetallesModel.eliminarEtiqueta(etiquetaAEliminar)

        // Verificación de que se llamó al método del repositorio con la etiqueta correcta
        Mockito.verify(etiquetaDetallesRepositorio).eliminarEtiqueta(etiquetaAEliminar)
    }
}