package com.example.tustareas.util

import java.util.Calendar
import java.util.Date

object DateHelper {
    fun timestampToString(fecha: Date?): String {
        var devolver = ""
        // Control de la causistica no has definido una fecha (null)
        if (fecha == null) {
            // asignacion a calendar
            val calendar = Calendar.getInstance()
            calendar.time = fecha
            // Obtencion año, mes y dia de la fecha
            val ano = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)
            devolver = "$dia/$mes/$ano"
        }

        return devolver
    }
}