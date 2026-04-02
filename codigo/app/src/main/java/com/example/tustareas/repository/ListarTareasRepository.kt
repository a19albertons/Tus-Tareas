package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea

/**
 * Clase que va representar el subrepositorio que hara las consultas contra el dao de inicio
 */
class ListarTareasRepository(database: TusTareasDatabase) {
    private val listarTareasConsultas = database.listarTareasConsultas()
    // Filtrar tareas
    fun obtenerTareasFiltradas(prioridad: Array<Prioridad>, estado: Array<Estado>, textoTarea: String, orden: OrdenarTareas) =
        when (orden) {
            OrdenarTareas.FECHA_CREACION_ASC -> listarTareasConsultas.obtenerTareasFiltradasFechaCreacionAsc(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_CREACION_DES -> listarTareasConsultas.obtenerTareasFiltradasFechaCreacionDes(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_LIMITE_ASC -> listarTareasConsultas.obtenerTareasFiltradasFechaLimiteAsc(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_LIMITE_DES -> listarTareasConsultas.obtenerTareasFiltradasFechaLimiteDes(prioridad, estado, textoTarea)
        }

    suspend fun modificarTarea(tarea: Tarea) = listarTareasConsultas.modificarTarea(tarea)
}