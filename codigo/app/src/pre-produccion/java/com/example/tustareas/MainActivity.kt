package com.example.tustareas

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.util.LanguageHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Clase principal del proyecto que representa la actividad
 * Ramificación del Main Activity original para el entorno de pre-producción. La principal diferencia es que no gestion el sqlCypher en el splash screen.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // Variables generales/compartidas entre 1 o varias funcines
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val model: TusTareasModel by viewModels()

    // Listado fragmentos sin flecha de retroceso
    private val fragmentosSinFlecha =
        setOf(
            R.id.inicioFragment,
            R.id.listarTareasFragment,
            R.id.listarProyectosFragment,
            R.id.estadisticasFragment,
            R.id.ajustesFragment,
            R.id.listarEtiquetasFragment,
        )

    /**
     * Metodo que crea la actividad y configura la base de la aplicación
     *
     * @param savedInstanceState El estado guardado de la actividad, si existe
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Habilita el modo oscuro segun la preferencia guardada
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val modo = prefs.getInt("tema", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(modo)

        // Asegurar aplicación idioma
        val idiomaGuardado = prefs.getString("idioma", "Sistema") ?: "Sistema"
        LanguageHelper.aplicarIdioma(LanguageHelper.etiquetaIdioma(idiomaGuardado))

        // Muestra la pantalla de carga antes de que se empiece a dibujar el primer frame del activyty y su fragmento
        // Adiconalmente espera a que la base de datos este lista para ocultar la pantalla de carga.
        installSplashScreen()

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

        // Permiso para notificaicones
        val permisoNotificaciones =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { respuesta: Boolean ->
                if (respuesta) {
                    Log.i("MainActivity", "Permiso concedido para notificar")
                } else {
                    Log.i("MainActivity", "Permiso denegado para notificar")
                }
            }
        val notificacionPermiso = prefs.getBoolean("notificacion", false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificacionPermiso) {
            // Pasa de false a true una vez preguntado
            prefs.edit { putBoolean("notificacion", true) }
            // Pregunta por el permiso de notificaciones
            permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Permiso para alarma
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val alarmManager = getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                // Valor en caso de no estar preguntado nunca
                val alarmaPermiso = prefs.getBoolean("alarma", false)

                if (!alarmaPermiso) {
                    // Pasa de false a true
                    prefs.edit { putBoolean("alarma", true) }
                    // Pregunta pro el permiso de alarmas
                    val intent =
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                            data = Uri.fromParts("package", packageName, null)
                        }
                    startActivity(intent)
                }
            }
        }

        // Llama a la logica de trabajadores
        model.trabajadores()

        // Llama a la logica de notificaciones
        lifecycleScope.launch {
            model.notificaciones(intent)
        }
    }

    /**
     * Infla el menú de opciones en la barra de herramientas (toolbar) de la actividad.
     *
     * @param menu El menú en el que se inflará el archivo de menú XML
     * @return true para mostrar el menú, false para no mostrarlo
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_general, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Gestiona la selección de elementos del menú en la toolbar.
     *
     * @param item El elemento del menú seleccionado
     * @return true si el evento fue manejado, false para permitir que otros lo manejen
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
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
                } catch (_: Exception) {
                    Snackbar.make(findViewById(R.id.main), getString(R.string.error_eliminando_tareas), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            navController,
        ) ||
            super.onOptionsItemSelected(item)
    }

    /**
     * Gestiona la acción de navegación hacia arriba (flecha de retroceso) en la barra de herramientas.
     *
     * @return true si la navegación hacia arriba fue manejada, false para permitir que otros lo manejen
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
}
