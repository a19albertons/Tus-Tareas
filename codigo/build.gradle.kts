// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false

    // KSP procesa las anotacioones de Room y es necesario para Hilt (el de este fichero oficialmente, no).
    alias(libs.plugins.ksp) apply false

    // Safe Args para pasar parametro de una clase a otra
    alias(libs.plugins.androidx.navigation.safeargs) apply false

    // Parcelize para poder pasar un clase con @Parcelize a otro fragmento o actividad
    alias(libs.plugins.kotlin.parcelize) apply false

    // Generación de documentación con Dokka
    alias(libs.plugins.kotlin.dokka) apply false

    // Hilt para inyección de dependencias
    alias(libs.plugins.hilt) apply false
}