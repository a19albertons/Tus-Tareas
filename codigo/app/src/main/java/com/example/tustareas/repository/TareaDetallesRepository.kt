package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Tarea

/**
 * Clase que gestiona el subrepositorio de tarea detalles
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TareaDetallesRepository(database: TusTareasDatabase) {
    private val tareaDetallesConsulta = database.tareaDetallesConsulta()

    /**
     * Obtiene una tarea por su id
     *
     * @param id El ID de la tarea a obtener
     * @return La tarea correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareaDTOPorID(id: Int) = tareaDetallesConsulta.obtenerTareaDTOPorID(id)

    /**
     * Elimina una tarea de la base de datos
     *
     * @param tarea La tarea a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarTarea(tarea: Tarea) = tareaDetallesConsulta.eliminarTarea(tarea)
}