package com.example.tustareas.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tustareas.util.AlarmaHelper

/**
 * Clase que reactiva las alarmas al reiniciarse o encender el dispositivo
 */
class RestaurarAlarmasReinicio: BroadcastReceiver() {
    override fun onReceive(contexto: Context?, intent: Intent?) {
        // Comprueba si la action es la de reinicio/encendido de movil
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Programa la alarma para media noche
            AlarmaHelper.programarAlarmaDiaria(contexto)
        }
    }
}