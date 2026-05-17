package com.example.tustareas.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tustareas.repository.WorkerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Clase que gestiona el paso de tareas que exceden su tiempo limite y aún no estan completadas de acuerdo al usuario
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param appContext el contexto de la aplicación, necesario para el Worker
 * @param workerParams los parámetros del Worker, necesarios para el Worker
 * @param repositorio el repositorio que alberga los metodos de actualizar Estado
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltWorker
class ActualizarEstadoWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val repositorio: WorkerRepository,
    ) : Worker(appContext, workerParams) {
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
            } catch (_: Exception) {
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
