package com.example.tustareas.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Notificacion
import com.example.tustareas.repository.WorkerRepository
import com.example.tustareas.util.AlarmaHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Clase que gestiona la alarma y la ejecuta
 */
class LanzarNotificaciones  : BroadcastReceiver() {
    override fun onReceive(contexto: Context?, intent: Intent?) {
        // Recrear la alarma para mañana
        AlarmaHelper.programarAlarmaDiaria(contexto?.applicationContext)
        val pendingResult = goAsync() // Permite tareas en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Variables para las consultas
                val db = TusTareasDatabase.getDatabase(contexto!!)
                val repository = WorkerRepository(db)

                // Obtener notificaciones
                val notificaciones = repository.obtenerTodasLasNotificaciones()

                // Lista tareas retrasadas
                val tareasRetrasadas = repository.tareasRetrasadasAlarma()

                // Filtramos y comprobamos si la tarea retrasada es nueva
                for (tarea in tareasRetrasadas) {
                    if (tarea.id !in notificaciones.map { it.idTarea }) {
                        repository.anadirNotificacion(Notificacion(0, "Retrasada - ${tarea.nombre}", tarea.descripcion?:"", false, tarea.id))
                    }
                }

                // Reenvio de notificaciones no leidas
                val enviarNotificaciones = repository.enviarNotificaciones()



                // Vuelve al main para lanzar las tareas
                withContext(Dispatchers.Main) {
                    // Lanzar notificaciones
                    AlarmaHelper.invocarAlarma(contexto, enviarNotificaciones)

                }
            } finally {
                pendingResult.finish() // Finaliza el trabajo en segundo plano
            }

        }
    }
}
