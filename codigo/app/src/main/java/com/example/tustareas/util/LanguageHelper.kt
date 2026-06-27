package com.example.tustareas.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Clase que ayuda con las funciones especificas de idioma
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
object LanguageHelper {
    /**
     * Obtiene el tag interno de un idioma
     *
     * @param idioma El idioma del que se quiere obtener el tag interno
     * @return El tag interno del idioma proporcionado. Si el idioma no es reconocido, devuelve una cadena vacía
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun etiquetaIdioma(idioma: String): String = IdiomaApp.entries.find { it.nombre == idioma }?.tag ?: ""

    /**
     * Aplica el idioma a partir del tag proporcionado
     *
     * @param siglas El tag del idioma a aplicar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun aplicarIdioma(siglas: String) {
        val locale = LocaleListCompat.forLanguageTags(siglas)
        AppCompatDelegate.setApplicationLocales(locale)
    }
}
