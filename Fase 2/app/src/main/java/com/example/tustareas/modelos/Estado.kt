package com.example.tustareas.modelos

import com.example.tustareas.R

/**
 * Clase que representa el enum Estado
 */
enum class Estado {
    EnTiempo,
    Retrasada,
    Completada;

    fun labelRes(): Int = when (this) {
        EnTiempo -> R.string.en_tiempo
        Retrasada -> R.string.retrasada
        Completada -> R.string.completada
    }
}