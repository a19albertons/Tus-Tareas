package com.example.tustareas

import androidx.appcompat.app.AppCompatDelegate
import com.example.tustareas.util.LanguageHelper
import org.junit.Test

/**
 * Clase que gestiona las pruebas unitarias del language helper
 */
class LanguageHelperTest {

    // Test para comprobar la etiqueta del español
    @Test
    fun testEspanol() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Español")
        assert(etiqueta == "es")
    }

    // Test para comprobar la etqiueta del gallego
    @Test
    fun testGallego() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Gallego")
        assert(etiqueta == "gl")
    }

    // Test para comprobar la etiqueta del sistema
    @Test
    fun testSistema() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Sistema")
        assert(etiqueta == "")
    }

    // Test para comprobar la etiqueta del ingles
    @Test
    fun testIngles() {
        val etiqueta = LanguageHelper.etiquetaIdioma("Ingles")
        assert(etiqueta == "en")
    }

    // Test para comprobar la etiqueta de cualquier idioma
    @Test
    fun testCualquierIdioma() {
        val etiqueta = LanguageHelper.etiquetaIdioma("idioma")
        assert(etiqueta == "")
    }

    // Test de aplicar un idioma
    @Test
    fun aplicarIdioma() {
        LanguageHelper.aplicarIdioma("es")
        assert(AppCompatDelegate.getApplicationLocales().get(0)!!.language == "es")
    }
}