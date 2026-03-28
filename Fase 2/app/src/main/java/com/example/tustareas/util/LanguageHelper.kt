package com.example.tustareas.util

import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageHelper {
    fun etiquetaIdioma(idioma: String): String {
        return when (idioma) {
            "Sistema" -> ""
            "Español" -> "es"
            "Ingles" -> "en"
            "Gallego" -> "gl"
            else -> ""
        }
    }
    fun aplicarIdioma(siglas: String) {
        val locale = LocaleListCompat.forLanguageTags(siglas ?: "")
        AppCompatDelegate.setApplicationLocales(locale)

    }
}