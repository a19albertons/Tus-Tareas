// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // Con kotlin 2.2.0 usar esta versión en adelante o hay problema con el dsl
    id("com.google.devtools.ksp") version "2.3.4" apply false
}