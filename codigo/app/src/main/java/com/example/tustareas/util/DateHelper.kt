package com.example.tustareas.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
            // Reemplazamos la codificacíón manual por una función del sistema
            devolver = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        }

        return devolver
    }
}