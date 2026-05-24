package com.example.tustareas.modelos

import com.example.tustareas.R

/**
 * Clase que representa el enum Estado
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
enum class Estado {
    EN_TIEMPO,
    RETRASADA,
    COMPLETADA,
    ;

    fun labelRes(): Int =
        when (this) {
            EN_TIEMPO -> R.string.en_tiempo
            RETRASADA -> R.string.retrasada
            COMPLETADA -> R.string.completada
        }
}
