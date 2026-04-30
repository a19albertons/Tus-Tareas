package com.example.tustareas.util

import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Clase que ayuda con las funciones especificas de fechas
 */
object DateHelper {
    // Variable para simular fechas en pruebas
    var fechaSimulada: Date? = null

    /**
     * Devuelve la fecha de hoy (medianoche) normalizada a UTC para evitar desfases
     */
    fun fechaMediaNocheUTC(): Date {
        val cal = Calendar.getInstance()
        // Si hay una fecha simulada, la usamos
        fechaSimulada?.let { cal.time = it }

        val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        utcCal.set(Calendar.MILLISECOND, 0)
        return utcCal.time
    }
}