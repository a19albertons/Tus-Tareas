package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Notificacion

/**
 * Clase que representa al repositorio de las tareas programadas
 */
class WorkerRepository(database: TusTareasDatabase) {
    private val workerConsultas = database.workerConsultas()

    // Actualzia los estados de las taras
    fun actualizarEstado() = workerConsultas.actualizarEstado()

    // Metodo de la alarma para obtener tareas retrasadas
    suspend fun tareasRetrasadasAlarma() = workerConsultas.tareasRetrasadasAlarma()

    // Recupera todas las notifiaciones
    fun obtenerTodasLasNotificaciones() = workerConsultas.obtenerTodasLasNotificaciones()

    // Añade nuevas notificaciones no creadas de las tareas
    fun anadirNotificacion(notificacion: Notificacion) = workerConsultas.anadirNotificacion(notificacion)

    // Envia las notificaciones no leidas
    suspend fun enviarNotificaciones() = workerConsultas.enviarNotificaciones()

}