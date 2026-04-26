package com.example.tustareas.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Clase que ayuda con las funciones especificas de fechas
 */
object DateHelper {
    fun timestampToString(fecha: Date?): String {
        // Si es nula devuelve vacio
        var devolver = ""
        // Control de la causistica no has definido una fecha (null)
        if (fecha != null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            // Forzamos a que interprete el Date en UTC, ya que así se guarda en la BD
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            devolver = sdf.format(fecha)
        }

        return devolver
    }

    /**
     * Devuelve la fecha de hoy (medianoche) normalizada a UTC para evitar desfases
     */
    fun fechaMediaNocheUTC(): Date {
        val cal = Calendar.getInstance()
        val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        utcCal.set(Calendar.MILLISECOND, 0)
        return utcCal.time
    }
}