package com.example.tustareas.util

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.example.tustareas.HiltTestActivity
import com.example.tustareas.R

/**
 * launchFragmentInContainer no es compatible con Hilt porque usa una actividad interna
 * que no tiene la anotación @AndroidEntryPoint.
 * Esta utilidad lanza el fragmento dentro de nuestra HiltTestActivity.
 *
 * Añade un metodo nuevo a la clase FragmentScenario.
 */
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_TusTareas,
    crossinline action: T.() -> Unit = {}
) : ActivityScenario<HiltTestActivity> {
    // Inicia la nueva actividad HiltTestActivity
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    )

    // Lo que devolvemos
    val scenario = ActivityScenario.launch<HiltTestActivity>(startActivityIntent)


    // Crea el fragmento dentro de la actividad anteriormente creada
    scenario.onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        (fragment as T).action()
    }

    return scenario
}