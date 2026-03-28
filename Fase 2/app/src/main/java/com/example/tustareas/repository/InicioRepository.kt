package com.example.tustareas.repository

import com.example.tustareas.dao.InicioConsultas
import com.example.tustareas.db.TusTareasDatabase
import java.util.Date

/**
 * Clase que va representar el subrepositorio que hara las consultas contra el dao de inicio
 */
class InicioRepository(database: TusTareasDatabase) {
    // Obtención del dao
    private val InicioConsultas = database.inicioConsultas()


    // Tareas en tiempo no completas que terminen hoy
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = InicioConsultas.obtenerTareasTerminanDiaEspecifico(fecha)

    // Tareas no completadas y retrasadas
    fun obtenerTareasRetrasadas() = InicioConsultas.obtenerTareasRetrasadas()

    // Tareas proximas
    fun obtenerTareasProximas(fecha: Date) = InicioConsultas.obtenerTareasProximas(fecha)
}