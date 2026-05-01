package com.example.tustareas.modelView

import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de tarea detalles
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TareaDetallesModel(private val repository: TusTareasRepository) {
    /**
     * Obtiene una tarea DTO por su id
     *
     * @param id El ID de la tarea a obtener
     * @return La tarea DTO correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareaDTOPorID(id: Int) = repository.tareaDetalles.obtenerTareaDTOPorID(id)

    /**
     * Elimina una tarea de la base de datos
     *
     * @param tarea La tarea a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarTarea(tarea: Tarea) = repository.tareaDetalles.eliminarTarea(tarea)

}