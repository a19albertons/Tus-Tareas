package com.example.tustareas.modelView

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.repository.TusTareasRepository
import kotlin.apply
import androidx.core.content.edit
import androidx.lifecycle.application
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tustareas.util.AlarmaHelper
import com.example.tustareas.util.LanguageHelper
import com.example.tustareas.workers.ActualizarEstadoWorker
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * ViewModel que une la aplicacion con la base de datos
 */
// El @JvmOverloads constructor es necesario porque por defecto parece
// no aceptar el constructor con un parámetros por defecto
class TusTareasModel @JvmOverloads constructor(
    application: Application,
    // Invocacion repositorio
    // si no le pasamos nada mantiene la retrocompatiblidad. Solo pasarle algo en pruebas de integració y otros tipos
    private val repository : TusTareasRepository = TusTareasRepository(TusTareasDatabase.getDatabase( application))
): AndroidViewModel(application) {


    // Submodelos de los distintos fragmentos del proyecto
    val inicio = InicioModel(repository)
    val verMas = VerMasModel(repository)
    val listarTareas = ListarTareasModel(repository, viewModelScope)
    val tareaDetalles = TareaDetallesModel(repository)
    val modificarTareas = ModificarTareasModel(repository)
    val listarProyectos = ListarProyectosModel(repository)
    val proyectoDetalles = ProyectoDetallesModel(repository)
    val modificarProyectos = ModificarProyectosModel(repository)
    val estadisticas = EstadisticasModel(repository)
    val listarEtiquetas = ListarEtiquetasModel(repository)
    val etiquetaDetalles = EtiquetaDetallesModel(repository)
    val modificarEtiquetas = ModificarEtiquetasModel(repository)



    // Metodo de la actividad principal
    suspend fun limpiarTareasCompletas() = repository.limpiarTareasCompletas()


    // Ajustes
    private val prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    // Idioma
    val idioma = MutableLiveData<String>().apply {
        value = prefs.getString("idioma", "sistema")
    }

    fun setIdioma(idioma: String) {
        prefs.edit { putString("idioma", idioma) }
        this.idioma.value = idioma
        LanguageHelper.aplicarIdioma(LanguageHelper.etiquetaIdioma(idioma))
    }

    val tema = MutableLiveData<Int>().apply {
        // Valor del sistema (movil)
        value = prefs.getInt("tema", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    // Modo claro/oscuro/sistema
    fun setTema (tema: Int) {
        prefs.edit { putInt("tema", tema) }
        this.tema.value = tema
        AppCompatDelegate.setDefaultNightMode(tema)
    }

    // Llama al repositorios para marcar como leida
    private suspend fun marcarNotificacionComoLeida(idNotificacion: Int) = repository.marcarNotificacionComoLeida(idNotificacion)

    fun trabajadores () {
        // trabajador 1 (cambio estados) - Lo configuramos para una ejecución diaria
        val ahoraMismo = Calendar.getInstance()
        // Definimos la fecha de configuración
        val fechaEjecucion = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0) // A las 0 horas
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        // Forzar la primera ejecución a las 0 horas del dia siguiente
        fechaEjecucion.add(Calendar.DAY_OF_MONTH, 1)
        val calcularRetraso = fechaEjecucion.timeInMillis - ahoraMismo.timeInMillis
        // Configuramos el worker
        val actualizarEstadoWorker = PeriodicWorkRequestBuilder<ActualizarEstadoWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(calcularRetraso, TimeUnit.MILLISECONDS)
            .build()

        // Mandamos el trabajo
        WorkManager.getInstance(application).enqueueUniquePeriodicWork("ActualizarEstado", ExistingPeriodicWorkPolicy.KEEP, actualizarEstadoWorker)
    }

    suspend fun notificaciones(intent: Intent) {
        // Alertas/notificaciones
        AlarmaHelper.programarAlarmaDiaria(application)

        // Comprobamos si entramos por notificación
        val idNotificacion = intent.getIntExtra("idNotificacion",-1)
        if (idNotificacion != -1) {
            // Marcamos como leida
            try {
                marcarNotificacionComoLeida(idNotificacion)
            }
            catch (_: Exception) {
                Log.e("MainActivity","Error silencioso al fallar en marcar notificacion como leida")
            }
        } else {
            Log.i("MainActivity","No se ha recibido notificacion")
        }
    }

}