package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase

/**
 * Clase que representa al repositorio de las tareas programadas
 */
class WorkerRepository(database: TusTareasDatabase) {
    private val workerConsultas = database.workerConsultas()

    fun actualizarEstado() = workerConsultas.actualizarEstado()
}