package com.example.tustareas.util

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.example.tustareas.modelos.Notificacion
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowNotificationManager

/**
 * Tests unitarios para la lógica de permisos en NotificacionesHelper.
 * Cubre los dos caminos: permiso concedido y permiso denegado.
 */
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class NotificacionesHelperPermissionTest {
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
     * T1: Permiso concedido en API 33+ → la notificación se publica.
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.TIRAMISU)
    fun `T1 crearNotificacion con permiso concedido en API 33+ publica la notificacion`() {
        // Arrange: conceder permiso POST_NOTIFICATIONS
        shadowOf(context as Application).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)

        val notificacion = Notificacion(id = 999, titulo = "Tarea Atrasada", mensaje = "Revisa tu tarea", leido = false, idTarea = 1)

        // Act
        NotificacionesHelper.crearNotificacion(context, notificacion)

        // Assert: verificar que se llamó a notify() con el shadow
        val shadowNotificationManager: ShadowNotificationManager = shadowOf(notificationManager)
        assertTrue("La notificación debería haberse publicado", shadowNotificationManager.getNotification(999) != null)
    }

    /**
     * T2: Permiso denegado en API 33+ → la notificación NO se publica.
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.TIRAMISU)
    fun `T2 crearNotificacion con permiso denegado en API 33+ no publica la notificacion`() {
        // Arrange: NO conceder permiso POST_NOTIFICATIONS (por defecto está denegado)

        val notificacion = Notificacion(id = 998, titulo = "Tarea Atrasada", mensaje = "Revisa tu tarea", leido = false, idTarea = 2)

        // Act
        NotificacionesHelper.crearNotificacion(context, notificacion)

        // Assert: verificar que NO se llamó a notify()
        val shadowNotificationManager: ShadowNotificationManager = shadowOf(notificationManager)
        assertFalse("La notificación NO debería haberse publicado sin permiso", shadowNotificationManager.getNotification(998) != null)
    }

    /**
     * T3: API < 33 (no se requiere POST_NOTIFICATIONS) → la notificación se publica.
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.O, maxSdk = Build.VERSION_CODES.S_V2)
    fun `T3 crearNotificacion en API menor a 33 no verifica permiso y publica`() {
        // En APIs anteriores a TIRAMISU, ActivityCompat.checkSelfPermission siempre devuelve PERMISSION_GRANTED
        // para permisos que no existen en esa versión

        val notificacion = Notificacion(id = 997, titulo = "Tarea Atrasada", mensaje = "Revisa tu tarea", leido = false, idTarea = 3)

        // Act
        NotificacionesHelper.crearNotificacion(context, notificacion)

        // Assert: la notificación debería publicarse sin problema
        val shadowNotificationManager: ShadowNotificationManager = shadowOf(notificationManager)
        assertTrue("La notificación debería haberse publicado en API < 33", shadowNotificationManager.getNotification(997) != null)
    }

    /**
     * T4: Permiso denegado + múltiples notificaciones → no crash, manejo graceful.
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.TIRAMISU)
    fun `T4 crearNotificacion multiple con permiso denegado no causa crash`() {
        // Arrange: NO conceder permiso

        val notificaciones =
            listOf(
                Notificacion(id = 901, titulo = "Tarea 1", mensaje = "Mensaje 1", leido = false, idTarea = 10),
                Notificacion(id = 902, titulo = "Tarea 2", mensaje = "Mensaje 2", leido = false, idTarea = 11),
                Notificacion(id = 903, titulo = "Tarea 3", mensaje = "Mensaje 3", leido = false, idTarea = 12),
            )

        // Act: llamar varias veces sin permiso → no debe crash
        notificaciones.forEach { notif ->
            NotificacionesHelper.crearNotificacion(context, notif)
        }

        // Assert: ninguna debería publicarse
        val shadowNotificationManager: ShadowNotificationManager = shadowOf(notificationManager)
        assertFalse("Ninguna notificación debería haberse publicado", shadowNotificationManager.getNotification(901) != null)
        assertFalse("Ninguna notificación debería haberse publicado", shadowNotificationManager.getNotification(902) != null)
        assertFalse("Ninguna notificación debería haberse publicado", shadowNotificationManager.getNotification(903) != null)
    }

    /**
     * T5: Permiso concedido + canal no creado → la notificación se publica con el canal por defecto.
     */
    @Test
    @Config(minSdk = Build.VERSION_CODES.TIRAMISU)
    fun `T5 crearNotificacion sin canal previo crea notificacion correctamente`() {
        // Arrange: conceder permiso y asegurar que no hay canal previo
        shadowOf(context as Application).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)

        val notificacion = Notificacion(id = 900, titulo = "Test", mensaje = "Sin canal previo", leido = false, idTarea = 20)

        // Act: crear notificación sin haber creado el canal primero
        NotificacionesHelper.crearNotificacion(context, notificacion)

        // Assert: Robolectric maneja canales automáticamente en tests
        val shadowNotificationManager: ShadowNotificationManager = shadowOf(notificationManager)
        assertTrue("La notificación debería publicarse incluso sin canal explícito", shadowNotificationManager.getNotification(900) != null)
    }
}
