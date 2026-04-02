package com.example.tustareas.modelView

import android.app.Application
import com.example.tustareas.repository.TusTareasRepository
import java.util.Date

/**
 * Clase que va representar el submodelo de tus tareas para el fragmento de inicio
 */
class InicioModel(private val repository: TusTareasRepository) {
    // Llama al repositorio para obtener las tareas debidas en cada caso
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = repository.inicio.obtenerTareasTerminanDiaEspecifico(fecha)
    fun obtenerTareasRetrasadas() = repository.inicio.obtenerTareasRetrasadas()
    fun obtenerTareasProximas(fecha: Date) = repository.inicio.obtenerTareasProximas(fecha)
}