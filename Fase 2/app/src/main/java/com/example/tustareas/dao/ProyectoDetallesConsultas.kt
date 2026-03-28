package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Proyecto

/**
 * Clase que gestiona las consultas contra la bd de proyecto detalles
 */
@Dao
interface ProyectoDetallesConsultas {
    // Metodos que usan internamente solo aqui
    @Query("UPDATE tareas SET idProyecto = null where idProyecto = :id")
    suspend fun eliminarProyectoID(id: Int)
    @Delete
    suspend fun eliminarProyecto(proyecto: Proyecto)

    // Obtiene el id
    @Transaction
    @Query("SELECT * FROM proyectos where id = :id")
    fun obtenerProyectoPorId(id: Int): LiveData<ProyectoDTO>

    // Borra un proyecto y sus relaciones
    @Transaction
    suspend fun eliminarProyectoConTareaYEtiqueta(proyecto: Proyecto) {
        eliminarProyectoID(proyecto.id)
        eliminarProyecto(proyecto)
    }
}