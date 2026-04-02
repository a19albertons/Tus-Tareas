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
    fun crearNotificacion(contexto: Context, titulo: String, contenido: String, id: Int) {
        val builder = NotificationCompat.Builder(contexto, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(titulo)
            .setContentText(contenido)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contenido)) // Para poder mostrar el texto completo y no quedarse en ...
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Elimina la notificación al acceder a ella

        with(NotificationManagerCompat.from(contexto)) {
            // Comprueba si hay permiso para notificar algo que se empezo a requerir desde android 13
            if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Se tienen que usar ids distintos
                notify(id, builder.build())
            }
        }
    }

}