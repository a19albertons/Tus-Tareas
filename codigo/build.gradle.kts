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

    // Ktlint para formatear el código
    alias(libs.plugins.ktlint)
}

ktlint {
    // Aplica las reglas de estilo de Android
    android.set(true)
    // Falla si se encuentran errores de estilo
    ignoreFailures.set(false)
    // Genera informes de errores en formato texto y HTML
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
    }
}
