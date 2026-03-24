package com.example.tustareas.util

import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageHelper {
    fun etiquetaIdioma(idioma: String): String {
        return when (idioma) {
            "Sistema" -> "es"
            "Español" -> "es"
            "Ingles" -> "en"
            "Gallego" -> "gl"
            else -> "es"
        }
    }
    fun aplicarIdioma(siglas: String) {
        val locale = LocaleListCompat.forLanguageTags(siglas ?: "es")
        AppCompatDelegate.setApplicationLocales(locale)

    }
}