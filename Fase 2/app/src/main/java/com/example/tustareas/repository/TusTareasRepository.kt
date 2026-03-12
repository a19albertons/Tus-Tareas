package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import java.util.Date

class TusTareasRepository(database: TusTareasDatabase) {
    // Imporaction daos
    private val tareaConsultas = database.tareaConsultas()

    // funcines daos
    // Tareas en tiempo no completas que terminen hoy
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = tareaConsultas.obtenerTareasTerminanDiaEspecifico(fecha)

    // Tareas no completadas y retrasadas
    fun obtenerTareasRetrasadas(fecha: Date) = tareaConsultas.obtenerTareasRetrasadas(fecha)
}