package com.example.tustareas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.tustareas.modelView.TusTareasModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    // Variables generales/compartidas entre 1 o varias funcines
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val model : TusTareasModel by viewModels()

    // Listado fragmentos sin flecha de retroceso
    private val fragmentosSinFlecha = setOf(
        R.id.inicioFragment,
        R.id.listarTareasFragment,
        R.id.listarProyectosFragment,
        R.id.estadisticasFragment,
        R.id.ajustesFragment,
        R.id.listarEtiquetasFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Obtencion nav controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configuración basica toolbar - Barra superior
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configuración basica bottomnavigartionview - Barra inferior
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)

        // Define los destinos principales (sin flecha de retroceso)
        appBarConfiguration = AppBarConfiguration(fragmentosSinFlecha)

        // Maneja la flecha de retroceso teniendo en cuenta el appbarconfiguration
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)


        // Habilita el modo oscuro segun la preferencia guardada
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val modo = prefs.getInt("tema", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(modo)

    }

    // Inflado del menu toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_general, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Navegacion del menu toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Controlar a android id home
        if (item.itemId == android.R.id.home) {
            // Para que salten los distintos dialogos que se configuren
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        if (item.itemId == R.id.action_limpiar_tareas_completas) {

            lifecycleScope.launch {
                try {
                    model.limpiarTareasCompletas()
                }
                catch (_: Exception) {
                    Snackbar.make(findViewById(R.id.main), "Lo sentimos ha habido un \nerror eliminando las tareas", Snackbar.LENGTH_SHORT).show()
                }
            }


        }
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }

    // Modificación logica flecha de retroceso
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}