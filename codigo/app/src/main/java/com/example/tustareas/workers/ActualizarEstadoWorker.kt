package com.example.tustareas.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.repository.WorkerRepository

/**
 * Clase que gestiona el paso de tareas que exceden su tiempo limite y aún no estan completadas de acuerdo al usuario
 */
class ActualizarEstadoWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    // Sobreescribe el metodo que indica el trabajo
    override fun doWork(): Result {
        var devolver = Result.success()
        try {
            revisarEstados()
        }
        catch (_: Exception) {
            devolver = Result.failure()
        }


        return devolver
    }

    // Función que controla la logica de la tarea programada
    private fun revisarEstados() {
        val database = TusTareasDatabase.getDatabase(applicationContext)
        val repositorio = WorkerRepository(database)
        repositorio.actualizarEstado()
    }
}