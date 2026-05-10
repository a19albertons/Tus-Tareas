package com.example.tustareas

import android.app.Application
import android.content.Intent
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlin.jvm.java
import kotlin.system.exitProcess

/**
 * Clase Application personalizada para manejar errores no controlados a nivel global.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltAndroidApp
class TusTareasApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Obtiene un manejador de errorres de tipo por defecto
        val capturadorErrores = Thread.getDefaultUncaughtExceptionHandler()

        // Controla la captura de errores no controlados
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            // Si el packagename terminar por error invocamos este activity
            if (packageName.endsWith(":error_process")) {
                capturadorErrores?.uncaughtException(thread, throwable)
                return@setDefaultUncaughtExceptionHandler
            }


            // Obtener el stack trace completo
            val stackTrace = throwable.stackTraceToString()

            // Genera un log para logcat con el error no previsto
            Log.e("Error no previsto", stackTrace, throwable)

            // Lanzamos el activity que muestra el error al usuario.
            val intent = Intent(this, ErrorNoPrevisto::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("error", getString(R.string.error_no_previsto)+stackTrace)
            }
            // Lanzamos la actividad
            startActivity(intent)

            // MAtamos el activity previo y lo cerramos para evitar fugas de memoria y potenciales errores
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(10)
        }
    }
}