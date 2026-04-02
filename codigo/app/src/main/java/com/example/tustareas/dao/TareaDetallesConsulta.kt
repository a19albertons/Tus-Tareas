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
 */
@Dao
interface TareaDetallesConsulta {
    // Obtencion tareas dto
    @Transaction
    @Query("select * from tareas where id = :id")
    fun obtenerTareaDTOPorID(id: Int): LiveData<TareaDTO>

    // Eliminar tarea
    @Delete
    suspend fun eliminarTarea(tarea: Tarea)
}