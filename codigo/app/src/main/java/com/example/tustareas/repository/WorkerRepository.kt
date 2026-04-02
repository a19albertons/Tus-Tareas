package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Notificacion

/**
 * Clase que representa al repositorio de las tareas programadas
 */
class WorkerRepository(database: TusTareasDatabase) {
    private val workerConsultas = database.workerConsultas()

    fun actualizarEstado() = workerConsultas.actualizarEstado()
    suspend fun tareasRetrasadasAlarma() = workerConsultas.tareasRetrasadasAlarma()

    fun obtenerTodasLasNotificaciones() = workerConsultas.obtenerTodasLasNotificaciones()

    fun anadirNotificacion(notificacion: Notificacion) = workerConsultas.anadirNotificacion(notificacion)

    suspend fun enviarNotificaciones() = workerConsultas.enviarNotificaciones()

}