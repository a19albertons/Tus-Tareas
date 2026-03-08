package com.example.tustareas.modelos

import androidx.room.TypeConverter
import java.util.Date

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
    fun fromDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toDate(value: Date): Long {
        return value.time
    }
}