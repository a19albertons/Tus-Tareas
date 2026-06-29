package com.example.tustareas.util

/**
 * Enumeración que define los idiomas de la aplicación.
 *
 * @param tag El tag del idioma
 * @param nombre El nombre del idioma
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
enum class IdiomaApp(
    val tag: String,
    val nombre: String,
) {
    SISTEMA("", "Sistema"),
    ESPANOL("es", "Español"),
    ITALIANO("it", "Italiano"),
    FRANCES("fr", "Francés"),
    ALEMAN("de", "Alemán"),
    PORTUGUES("pt", "Portugués"),
    INGLES("en", "Ingles"),
    GALLEGO("gl", "Gallego"),
}
