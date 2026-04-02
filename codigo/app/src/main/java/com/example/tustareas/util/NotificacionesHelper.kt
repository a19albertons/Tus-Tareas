package com.example.tustareas.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tustareas.MainActivity
import com.example.tustareas.R
import com.example.tustareas.modelos.Notificacion

/**
 * Helper que gestionara el canal y creacion de notificaciones
 */
object NotificacionesHelper {
    private const val CHANNEL_ID = "tusTareasNotificacionesID"
    private const val CHANNEL_NAME = "tusTareasNotificaciones"
    private const val CHANNEL_DESCRIPTION = "Notificaciones para tareas retrasadas"

    // Crea el cana
    fun crearCanalNotificaciones(context: Context) {
        // Comprueba si la versión es la 8.0 o superior porque hay una obligación de
        // crear canales a partir de esta versión de la api para notificar
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
    fun crearNotificacion(contexto: Context, notificacion: Notificacion) {
        // pending para que la notificación sea clickable
        val intent = Intent(contexto, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("idNotificacion", notificacion.id)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(contexto, notificacion.id, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(contexto, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(notificacion.titulo)
            .setContentText(notificacion.mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificacion.mensaje)) // Para poder mostrar el texto completo y no quedarse en ...
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // La notificación es clickable a una acción predeterminada
            .setAutoCancel(true) // Elimina la notificación al acceder a ella

        with(NotificationManagerCompat.from(contexto)) {
            // Comprueba si hay permiso para notificar algo que se empezo a requerir desde android 13
            if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Se tienen que usar ids distintos
                notify(notificacion.id, builder.build())
            }
        }
    }

}