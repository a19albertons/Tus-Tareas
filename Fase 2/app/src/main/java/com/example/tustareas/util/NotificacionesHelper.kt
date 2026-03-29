package com.example.tustareas.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tustareas.R

/**
 * Helper que gestionara el canal y creacion de notificaciones
 */
object NotificacionesHelper {
    private const val CHANNEL_ID = "tusTareasNotificacionesID"
    private const val CHANNEL_NAME = "tusTareasNotificaciones"
    private const val CHANNEL_DESCRIPTION = "Notificaciones para tareas retrasadas"

    // Crea el cana
    fun crearCanalNotificaciones(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Definimos la importancia
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            // Define el canal con la importancia
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importancia).apply {
                description = CHANNEL_DESCRIPTION
            }
            // Crea el canal manager con el canal
            val managerNotificiones: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            managerNotificiones.createNotificationChannel(channel)
        }
    }


    // Crea una notifiación
    fun crearNotificacion(contexto: Context, titulo: String, contenido: String ) {
        val builder = NotificationCompat.Builder(contexto, CHANNEL_ID)
            .setSmallIcon(R.drawable.fecha_fin) // provisional
            .setContentTitle(titulo)
            .setContentText(contenido)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contenido)) // Para poder mostrar el texto completo y no quedarse en ...
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Elimina la notificación al acceder a ella

        with(NotificationManagerCompat.from(contexto)) {
            if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }
    }

}