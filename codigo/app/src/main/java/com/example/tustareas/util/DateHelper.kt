package com.example.tustareas.util

import java.util.Calendar
import java.util.Date

/**
 * Clase que ayuda con las funciones especificas de fechas
 */
object DateHelper {
    fun timestampToString(fecha: Date?): String {
        // Si es nula devuelve vacio
        var devolver = ""
        // Control de la causistica no has definido una fecha (null)
        if (fecha != null) {
            // asignacion a calendar
            val calendar = Calendar.getInstance()
            calendar.time = fecha
            // Obtencion año, mes y dia de la fecha
            val ano = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)+1
            val dia = calendar.get(Calendar.DAY_OF_MONTH)
            devolver = "$dia/$mes/$ano"
        }

        return devolver
    }
}