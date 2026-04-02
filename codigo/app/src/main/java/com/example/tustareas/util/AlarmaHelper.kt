package com.example.tustareas.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.tustareas.modelos.Notificacion
import com.example.tustareas.workers.LanzarNotificaciones
import java.util.Calendar

object AlarmaHelper {
    fun invocarAlarma(context: Context, listaNotificacionesEnviar: List<Notificacion>) {
        if (listaNotificacionesEnviar.isNotEmpty()) {
            NotificacionesHelper.crearCanalNotificaciones(context)
            // Bucle que manda las notificaciones a enviar
            for (notificacion in listaNotificacionesEnviar) {
                NotificacionesHelper.crearNotificacion(context, notificacion )
            }

        }
    }

    fun programarAlarmaDiaria(context: Context?) {
        val alarmaManager = (context?.getSystemService(Context.ALARM_SERVICE)) as AlarmManager
        val intent = Intent(context, LanzarNotificaciones::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }

        // Comprueba si tiene una versión que no requiere el permiso
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Comprueba si tiene el permiso para alarmas
            if (alarmaManager.canScheduleExactAlarms()) {
                alarmaManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            else {
                Log.d("AlarmaHelper", "No se puede lanzar lar alarma no se tiene el permiso Schedule Exact Alarm")
            }
        }
        else {
            alarmaManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}