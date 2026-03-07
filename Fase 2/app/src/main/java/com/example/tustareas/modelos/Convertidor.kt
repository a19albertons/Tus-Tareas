package com.example.tustareas.modelos

import androidx.room.TypeConverter

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
}