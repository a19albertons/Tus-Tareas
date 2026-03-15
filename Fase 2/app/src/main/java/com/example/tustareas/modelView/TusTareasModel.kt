package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.repository.TusTareasRepository
import java.util.Date

/**
 * ViewModel que une la aplicacion con la base de datos
 */
class TusTareasModel(application: Application): AndroidViewModel(application) {
    // Invocacion repositorio
    private val repository = TusTareasRepository(TusTareasDatabase.getDatabase( application))

    // Metodos de la base de datos
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = repository.obtenerTareasTerminanDiaEspecifico(fecha)
    fun obtenerTareasRetrasadas(fecha: Date) = repository.obtenerTareasRetrasadas(fecha)
    fun obtenerTareasProximas(fecha: Date) = repository.obtenerTareasProximas(fecha)
    fun obtenerEtiquetasFiltradas(texto: String) = repository.obtenerEtiquetasFiltradas(texto)
    fun obtenerEtiquetaPorID(id: Int) = repository.obtenerEtiquetaPorID(id)


}