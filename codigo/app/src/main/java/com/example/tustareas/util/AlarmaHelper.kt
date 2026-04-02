package com.example.tustareas.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.workers.LanzarNotificaciones
import java.lang.Thread.sleep
import java.util.Calendar

object AlarmaHelper {
    fun invocarAlarma(context: Context, listaTareasRetrasadas: List<Tarea>) {
        if (listaTareasRetrasadas.isNotEmpty()) {
            NotificacionesHelper.crearCanalNotificaciones(context)
            for (tarea in listaTareasRetrasadas) {
                // Invocar notificacion
                NotificacionesHelper.crearNotificacion(context, tarea.nombre, tarea.descripcion ?: "", tarea.id )
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmaManager.canScheduleExactAlarms()) {
                alarmaManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
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