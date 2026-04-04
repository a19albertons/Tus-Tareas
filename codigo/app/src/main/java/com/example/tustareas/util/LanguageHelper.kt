package com.example.tustareas.util

import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Clase que ayuda con las funciones especificas de idioma
 */
object LanguageHelper {
    // Obtiene el tag interno de un idioma
    fun etiquetaIdioma(idioma: String): String {
        return when (idioma) {
            "Sistema" -> ""
            "Español" -> "es"
            "Ingles" -> "en"
            "Gallego" -> "gl"
            else -> ""
        }
    }
    // Aplica el idioma a partir del tag
    fun aplicarIdioma(siglas: String) {
        val locale = LocaleListCompat.forLanguageTags(siglas)
        AppCompatDelegate.setApplicationLocales(locale)

    }
}