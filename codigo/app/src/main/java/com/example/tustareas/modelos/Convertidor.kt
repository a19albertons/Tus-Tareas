package com.example.tustareas.modelos

import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Clase que gestiona las conversiones que requiere room para los enums y tipos especificos
 */
class Convertidor {
    // Prioridad
    @TypeConverter
    fun fromPrioridad(value: Prioridad): String {
        return value.name
    }

    @TypeConverter
    fun toPrioridad(value: String): Prioridad {
        return runCatching { Prioridad.valueOf(value) }.getOrDefault(Prioridad.NoEstablecido )
    }

    // Estado
    @TypeConverter
    fun fromEstado(value: Estado): String {
        return value.name
    }

    @TypeConverter
    fun toEstado(value: String): Estado {
        return runCatching { Estado.valueOf(value) }.getOrDefault( Estado.EnTiempo )
    }

    // Date
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

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}