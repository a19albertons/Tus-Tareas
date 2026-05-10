plugins {
    alias(libs.plugins.android.application)

    // KSP procesa las anotacioones de Room y es necesario para Hilt.
    alias(libs.plugins.ksp)

    // Safe Args para pasar parametro de una clase a otra
    alias(libs.plugins.androidx.navigation.safeargs)

    // Parcelize para poder pasar un clase con @Parcelize a otro fragmento o actividad
    alias(libs.plugins.kotlin.parcelize)

    // Generación de documentación con Dokka
    alias(libs.plugins.kotlin.dokka)

    // Hilt para inyección de dependencias
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.tustareas"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.tustareas"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // Cambiamos el runner original
        testInstrumentationRunner = "com.example.tustareas.hilt.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // Construción customizada para el profiler
        create("profileable") {
            initWith(getByName("release")) // Copia la configuración de release
            signingConfig = signingConfigs.getByName("debug") // Coge la firma que se genera para debug

            // Configuración específica para el perfilado
            isProfileable = true // Permite el perfilado en esta build que es practicamente la de lanzamiento


        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
    flavorDimensions += ("versiones")
    productFlavors {
        create("pre-produccion") {
            dimension="versiones"
            applicationIdSuffix=".pre"
            versionNameSuffix="-pre"
        }
        create("produccion") {
            dimension="versiones"
        }
    }

    // Personalizar la gestión de los layout por tipo asociado.
    sourceSets {
        getByName("main") {
            // Verisón moderna (no deprecada) del res.setSrcDirs()
            res.directories.apply {
                clear()
                addAll(
                    listOf(
                        // Alberga fragmentos
                        "src/main/res/fragmentos",

                        // Alberga adaptadores
                        "src/main/res/adaptadores",

                        // Alberga actividades
                        "src/main/res/actividades",

                        // Alberga otros layout
                        "src/main/res"
                    )
                )
            }
        }
    }
}

dependencies {
    // Librerias para el organizador de tareas en segundo plano de tareas proramadas
    implementation(libs.androidx.work.runtime.ktx)
    androidTestImplementation(libs.androidx.work.testing)
    implementation(libs.androidx.work.multiprocess)

    // graficos de barra libreria
    implementation(libs.mpandroidchart)

    // Room librerias
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.core)

    // Live data
    implementation(libs.androidx.lifecycle.livedata.core.ktx)

    // Librerias para pruebas unitarias y de android test
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // Cifrado bd
    implementation(libs.sqlcipher.android)

    // Pantalla de carga aplicación
    implementation(libs.androidx.core.splashscreen)

    // Hilt para inyección de dependencias
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.common)

    // Predeterminadas
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// https://github.com/Kotlin/dokka/issues/4472
// o error en terminal
dokka {
    dokkaSourceSets.configureEach {
        suppress.set(name != "produccionRelease")
    }
}