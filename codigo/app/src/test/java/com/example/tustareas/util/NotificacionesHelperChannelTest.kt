package com.example.tustareas.util

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

/**
 * Tests unitarios para la creación de canales de notificaciones.
 */
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class NotificacionesHelperChannelTest {

    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        // Limpiar canales previos para tests aislados
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel("tusTareasNotificacionesID")
        }
    }

    /**
     * T5: API >= O (26+) → el canal se crea con nombre, descripción e importancia correctos.
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    fun `T5 crearCanalNotificaciones en API 26+ crea el canal con configuracion correcta`() {
        // Act
        NotificacionesHelper.crearCanalNotificaciones(context)

        // Assert: verificar que el canal existe y tiene la configuración esperada
        val channel = notificationManager.getNotificationChannel("tusTareasNotificacionesID")
        assertNotNull("El canal debería existir", channel)
        assertEquals("El nombre del canal debería ser 'tusTareasNotificaciones'", "tusTareasNotificaciones", channel.name)
        assertEquals(
            "La descripción del canal debería ser 'Notificaciones para tareas retrasadas'",
            "Notificaciones para tareas retrasadas",
            channel.description,
        )
    }

    /**
     * T6: API < O → no se crea ningún canal y no hay crash.
     */
    @Test
    @Config(maxSdk = Build.VERSION_CODES.N_MR1)
    fun `T6 crearCanalNotificaciones en API menor a 26 no crea canal ni causa crash`() {
        // Act: llamar debería ser un no-op sin crash
        NotificacionesHelper.crearCanalNotificaciones(context)

        // Assert original modificado para API < 26
        val shadowManager = shadowOf(notificationManager)
        val canales = shadowManager.notificationChannels

        assertEquals("No debería haberse creado ningún canal en versiones anteriores a Oreo", 0, canales.size)
    }

    /**
     * T7: Crear el mismo canal dos veces no causa error (es seguro llamar varias veces).
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    fun `T7 crearCanalNotificaciones llamado multiples veces no causa error`() {
        // Act & Assert: llamar múltiples veces no debería lanzar excepción
        NotificacionesHelper.crearCanalNotificaciones(context)
        NotificacionesHelper.crearCanalNotificaciones(context)
        NotificacionesHelper.crearCanalNotificaciones(context)

        val channel = notificationManager.getNotificationChannel("tusTareasNotificacionesID")
        assertNotNull("El canal debería existir después de múltiples llamadas", channel)
    }

    /**
     * T8: El canal creado tiene IMPORTANCE_DEFAULT (no es silencioso ni crítico).
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    fun `T8 crearCanalNotificaciones crea canal con importancia default`() {
        // Act
        NotificacionesHelper.crearCanalNotificaciones(context)

        // Assert: verificar la importancia del canal
        val channel = notificationManager.getNotificationChannel("tusTareasNotificacionesID")
        assertNotNull("El canal debería existir", channel)
        assertEquals(
            "La importancia del canal debería ser IMPORTANCE_DEFAULT (3)",
            NotificationManager.IMPORTANCE_DEFAULT,
            channel.importance,
        )
    }

    /**
     * T9: Después de eliminar el canal y volver a crearlo, se recrea correctamente.
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    fun `T9 crearCanalNotificaciones despues de eliminar se recrea correctamente`() {
        // Arrange: crear y luego eliminar
        NotificacionesHelper.crearCanalNotificaciones(context)
        notificationManager.deleteNotificationChannel("tusTareasNotificacionesID")

        val channelAfterDelete = notificationManager.getNotificationChannel("tusTareasNotificacionesID")
        assertNull("El canal debería haber sido eliminado", channelAfterDelete)

        // Act: recrear el canal
        NotificacionesHelper.crearCanalNotificaciones(context)

        // Assert: verificar que se recreó correctamente
        val recreatedChannel = notificationManager.getNotificationChannel("tusTareasNotificacionesID")
        assertNotNull("El canal debería haberse recreado", recreatedChannel)
        assertEquals(
            "El nombre del canal recreado debería ser correcto",
            "tusTareasNotificaciones",
            recreatedChannel.name,
        )
    }
}
