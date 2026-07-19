package com.example.tustareas

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExampleInstrumentedTest {
    @get:Rule // <-- Añadir la regla
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject() // <-- Inicializar Hilt
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        when (appContext.packageName) {
            "com.example.tustareas" -> {
                assertEquals("com.example.tustareas", appContext.packageName)
            }
            "com.example.tustareas.pre" -> {
                assertEquals("com.example.tustareas.pre", appContext.packageName)
            }
            else -> {
                fail("El package name no es correcto")
            }
        }
    }
}
