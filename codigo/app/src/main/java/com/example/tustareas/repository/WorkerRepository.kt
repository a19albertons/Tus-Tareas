package com.example.tustareas.repository

import com.example.tustareas.dao.WorkerConsultas
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Notificacion

/**
 * Clase que representa al repositorio de las tareas programadas
 *
 * @param database La base de datos de la aplicación
 * @param workerConsultas Las consultas para el worker.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class WorkerRepository(
    database: TusTareasDatabase,
    private val workerConsultas : WorkerConsultas= database.workerConsultas()
) {


    // Actualzia los estados de las taras
    /**
     * Actualiza el estado de las tareas en la base de datos.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun actualizarEstado() = workerConsultas.actualizarEstado()

    /**
     * Obtiene las tareas retrasadas para la alarma.
     *
     * @return Las tareas retrasadas para la alarma.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun tareasRetrasadasAlarma() = workerConsultas.tareasRetrasadasAlarma()

    /**
     * Obtiene todas las notificaciones de la base de datos.
     *
     * @return Todas las notificaciones de la base de datos.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTodasLasNotificaciones() = workerConsultas.obtenerTodasLasNotificaciones()

    /**
     * Añade una nueva notificación a la base de datos.
     *
     * @param notificacion La notificación a añadir.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun anadirNotificacion(notificacion: Notificacion) = workerConsultas.anadirNotificacion(notificacion)


    /**
     * Envía las notificaciones no leídas
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun enviarNotificaciones() = workerConsultas.enviarNotificaciones()

}