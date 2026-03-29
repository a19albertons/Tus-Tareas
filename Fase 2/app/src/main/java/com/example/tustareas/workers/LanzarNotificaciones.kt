package com.example.tustareas.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tustareas.db.TusTareasDatabase
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
        AlarmaHelper.programarAlarmaDiaria(contexto!!)
        val pendingResult = goAsync() // Permite tareas en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Variables para las consultas
                val db = TusTareasDatabase.getDatabase(contexto)
                val repository = WorkerRepository(db)

                // Lista necesaria
                val tareasRetrasadas = repository.tareasRetrasadasAlarma()

                // Vuelve al main para lanzar las tareas
                withContext(Dispatchers.Main) {
                    // Lanzar notificaciones
                    AlarmaHelper.invocarAlarma(contexto, tareasRetrasadas)

                }
            } finally {
                pendingResult.finish() // Finaliza el trabajo en segundo plano
            }

        }
    }
}
