package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Tarea

/**
 * Consultas del fragmento tarea detalles
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface TareaDetallesConsulta {

    /**
     * Obtiene una tarea DTO por su ID
     *
     * @param id El ID de la tarea a obtener.
     * @return LiveData<TareaDTO> devuelve la tarea DTO obtenida por su ID.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Transaction
    @Query("select * from tareas where id = :id")
    fun obtenerTareaDTOPorID(id: Int): LiveData<TareaDTO>

    /**
     * Elimina una tarea
     *
     * @param tarea La tarea a eliminar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Delete
    suspend fun eliminarTarea(tarea: Tarea)
}