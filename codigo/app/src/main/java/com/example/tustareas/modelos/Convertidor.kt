package com.example.tustareas.modelos

import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Clase que gestiona las conversiones que requiere room para los enums y tipos especificos
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class Convertidor {

    /**
     * Convierte un valor de Prioridad a String para almacenarlo en la base de datos.
     *
     * @param value El valor de Prioridad a convertir.
     * @return El nombre del valor de Prioridad como String.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @TypeConverter
    fun fromPrioridad(value: Prioridad): String {
        return value.name
    }

    /**
     * Convierte un valor de String a Prioridad para recuperarlo de la base de datos.
     *
     * @param value El valor de String a convertir.
     * @return El valor de Prioridad correspondiente.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @TypeConverter
    fun toPrioridad(value: String): Prioridad {
        return runCatching { Prioridad.valueOf(value) }.getOrDefault(Prioridad.NoEstablecido )
    }

    /**
     * Convierte un valor de Estado a String para almacenarlo en la base de datos.
     *
     * @param value El valor de Estado a convertir.
     * @return El nombre del valor de Estado como String.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @TypeConverter
    fun fromEstado(value: Estado): String {
        return value.name
    }

    /**
     * Convierte un valor de String a Estado para recuperarlo de la base de datos.
     *
     * @param value El valor de String a convertir.
     * @return El valor de Estado correspondiente.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @TypeConverter
    fun toEstado(value: String): Estado {
        return runCatching { Estado.valueOf(value) }.getOrDefault( Estado.EnTiempo )
    }

    /**
     * Convierte un valor de Date a Long para almacenarlo en la base de datos.
     *
     * @param value El valor de Date a convertir.
     * @return El valor de Date convertido a Long (milisegundos desde epoch) o null si el valor es null.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @TypeConverter
    fun fromDate(value: Date?): Long? {
        if (value == null) return null
        // Conversion datetime a date normalizada a UTC para evitar desfases de zona horaria
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.time = value
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * Convierte un valor de Long a Date para recuperarlo de la base de datos.
     *
     * @param value El valor de Long a convertir (milisegundos desde epoch) o null.
     * @return El valor de Long convertido a Date o null si el valor es null.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}