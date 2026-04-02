package com.example.tustareas.modelos

import com.example.tustareas.R

/**
 * Clase que representa el enum Prioridad
 */
enum class Prioridad {
    Alta,
    Media,
    Baja,
    NoEstablecido;

    fun labelRes(): Int = when (this) {
        Alta -> R.string.alta
        Media -> R.string.media
        Baja -> R.string.baja
        NoEstablecido -> R.string.no_establecido
    }
}