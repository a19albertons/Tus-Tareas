package com.example.tustareas

import androidx.appcompat.app.AppCompatDelegate
import com.example.tustareas.util.LanguageHelper
import org.junit.Test

class LanguageHelperTest {

    @Test
    fun testEspanol() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Español")
        assert(etiqueta == "es")
    }

    @Test
    fun testGallego() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Gallego")
        assert(etiqueta == "gl")
    }

    @Test
    fun testSistema() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Sistema")
        assert(etiqueta == "")
    }

    @Test
    fun testIngles() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Ingles")
        assert(etiqueta == "en")
    }

    @Test
    fun testCualquierIdioma() {
        val etiqueta = LanguageHelper.etiquetaIdioma("idioma")
        assert(etiqueta == "")
    }

    @Test
    fun aplicarIdioma() {
        LanguageHelper.aplicarIdioma("es")
        assert(AppCompatDelegate.getApplicationLocales().get(0)!!.language == "es")
    }
}