package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import java.util.Date

class TusTareasRepository(database: TusTareasDatabase) {
    // Imporaction daos
    private val tareaConsultas = database.tareaConsultas()

    // funcines daos
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = tareaConsultas.obtenerTareasTerminanDiaEspecifico(fecha)
}