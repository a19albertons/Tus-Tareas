package com.example.tustareas.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tustareas.util.AlarmaHelper

/**
 * Clase que reactiva las alarmas al reiniciarse o encender el dispositivo
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class RestaurarAlarmasReinicio: BroadcastReceiver() {
    /**
     * Metodo que se ejecuta al recibir el broadcan de reinicio o encendido del dispositivo que programara la alarma para media noche
     */
    override fun onReceive(contexto: Context?, intent: Intent?) {
        // Comprueba si la action es la de reinicio/encendido de movil
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Programa la alarma para media noche
            AlarmaHelper.programarAlarmaDiaria(contexto)
        }
    }
}