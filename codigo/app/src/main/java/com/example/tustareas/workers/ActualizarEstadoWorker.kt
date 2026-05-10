package com.example.tustareas.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.repository.WorkerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Clase que gestiona el paso de tareas que exceden su tiempo limite y aún no estan completadas de acuerdo al usuario
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltWorker
class ActualizarEstadoWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repositorio : WorkerRepository
): Worker(appContext, workerParams) {
    // Sobreescribe el metodo que indica el trabajo
    /**
     * Realiza el trabajo de revisar las tareas y actualizar su estado si es necesario
     *
     * @return El resultado del trabajo, indicando si fue exitoso o si hubo un error
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
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
    /**
     * Revisa las tareas en la base de datos y actualiza su estado si han excedido su tiempo limite y aún no estan completadas
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun revisarEstados() {
        repositorio.actualizarEstado()
    }
}