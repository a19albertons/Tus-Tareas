package com.example.tustareas.modelView

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.util.AlarmaHelper
import com.example.tustareas.util.LanguageHelper
import com.example.tustareas.workers.ActualizarEstadoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ViewModel que une la aplicacion con la base de datos
 *
 * @constructor Crea un nuevo TusTareasModel con el repositorio de la base de datos. No inyectar
 * ningun repositorio es una inyección de dependencias para pruebas de integración en androidTests
 * @param application La aplicación de Android
 * @param repository El repositorio de la aplicación, por defecto se crea uno nuevo con la base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class TusTareasModel
    @Inject
    constructor(
        application: Application,
        private val repository: TusTareasRepository,
    ) : AndroidViewModel(application) {
        /**
         * Limpia las tareas completadas de la base de datos.
         *
         * @author Alberto Noceda <a19albertons@iesanclemente.net>
         */
        suspend fun limpiarTareasCompletas() = repository.limpiarTareasCompletas()

        // Ajustes
        private val prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Idioma
        val idioma =
            MutableLiveData<String>().apply {
                value = prefs.getString("idioma", "sistema")
            }

        /**
         * Actualiza el idioma de la aplicación y lo guarda en las preferencias compartidas.
         *
         * @param idioma El nuevo idioma a establecer, puede ser "sistema", "español", "inglés"... Para más información consultar el [LanguageHelper]
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun setIdioma(idioma: String) {
            prefs.edit { putString("idioma", idioma) }
            this.idioma.value = idioma
            LanguageHelper.aplicarIdioma(LanguageHelper.etiquetaIdioma(idioma))
        }

        val tema =
            MutableLiveData<Int>().apply {
                // Valor del sistema (movil)
                value = prefs.getInt("tema", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

        // Modo claro/oscuro/sistema

        /**
         * Actualiza el tema de la aplicación y lo guarda en las preferencias compartidas.
         *
         * @param tema El nuevo tema a establecer, puede ser AppCompatDelegate.MODE_NIGHT_NO (claro),
         * AppCompatDelegate.MODE_NIGHT_YES (oscuro) o AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM (sistema)
         */
        fun setTema(tema: Int) {
            prefs.edit { putInt("tema", tema) }
            this.tema.value = tema
            AppCompatDelegate.setDefaultNightMode(tema)
        }

        /**
         * Marca una notificación como leída en la base de datos.
         *
         * @param idNotificacion El ID de la notificación a marcar como leída
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        private suspend fun marcarNotificacionComoLeida(idNotificacion: Int) = repository.marcarNotificacionComoLeida(idNotificacion)

        /**
         * Configura los trabajadores de la aplicación. En este caso, configura un trabajador para actualizar el estado de las tareas a diario a las 0 horas.
         *
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun trabajadores() {
            // trabajador 1 (cambio estados) - Lo configuramos para una ejecución diaria
            val ahoraMismo = Calendar.getInstance()

            // Definimos la fecha de configuración
            val fechaEjecucion =
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0) // A las 0 horas
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

            // Forzar la primera ejecución a las 0 horas del dia siguiente
            fechaEjecucion.add(Calendar.DAY_OF_MONTH, 1)
            val calcularRetraso = fechaEjecucion.timeInMillis - ahoraMismo.timeInMillis

            // Configuramos el worker
            val actualizarEstadoWorker =
                PeriodicWorkRequestBuilder<ActualizarEstadoWorker>(
                    1,
                    TimeUnit.DAYS,
                ).setInitialDelay(calcularRetraso, TimeUnit.MILLISECONDS)
                    .build()

            // Mandamos el trabajo
            WorkManager
                .getInstance(
                    application,
                ).enqueueUniquePeriodicWork("ActualizarEstado", ExistingPeriodicWorkPolicy.KEEP, actualizarEstadoWorker)
        }

        /**
         * Configura las alarmas de la aplicación. En este caso, configura una alarma diaria para mostrar notificaciones.
         * Además, si se recibe una notificación, marca esa notificación como leída en la base de datos.
         *
         * @param intent El intent recibido al abrir la aplicación, que puede contener información sobre una notificación recibida
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        suspend fun notificaciones(intent: Intent) {
            // Alertas/notificaciones
            AlarmaHelper.programarAlarmaDiaria(application)

            // Comprobamos si entramos por notificación
            val idNotificacion = intent.getIntExtra("idNotificacion", -1)
            if (idNotificacion != -1) {
                // Marcamos como leida
                try {
                    marcarNotificacionComoLeida(idNotificacion)
                } catch (_: Exception) {
                    Log.e("MainActivity", "Error silencioso al fallar en marcar notificacion como leida")
                }
            } else {
                Log.i("MainActivity", "No se ha recibido notificacion")
            }
        }

    /**
     * Busca la ultima version de la app en github.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun buscarActualizacion() {
            val intent = Intent(Intent.ACTION_VIEW, "https://github.com/a19albertons/Tus-Tareas/releases/latest".toUri())
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            application.startActivity(intent)
        }
    }
