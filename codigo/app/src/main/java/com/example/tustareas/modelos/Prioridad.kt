package com.example.tustareas.modelos

import com.example.tustareas.R

/**
 * Clase que representa el enum Prioridad
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
enum class Prioridad {
    ALTA,
    MEDIA,
    BAJA,
    NO_ESTABLECIDO;

    fun labelRes(): Int = when (this) {
        ALTA -> R.string.alta
        MEDIA -> R.string.media
        BAJA -> R.string.baja
        NO_ESTABLECIDO -> R.string.no_establecido
    }
}