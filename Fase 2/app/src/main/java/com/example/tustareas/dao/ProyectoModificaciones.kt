package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.ProyectoEtiqueta

@Dao
interface ProyectoModificaciones {
    // Usar solo aqui hasta el primer transaction
    @Insert
    suspend fun insertarProyecto(proyecto: Proyecto) : Long
    @Update
    suspend fun modificarProyecto(proyecto: Proyecto)
    @Insert
    suspend fun insertarProyectoEtiqueta(proyectoEtiqueta: ProyectoEtiqueta)
    @Query("DELETE FROM ProyectoEtiquetas WHERE idProyecto = :id")
    suspend fun eliminarRelacionProyectoEtiqueta(id: Int)
    @Query("UPDATE tareas SET idProyecto = null where idProyecto = :id")
    suspend fun eliminarProyectoID(id: Int)
    @Query("UPDATE tareas SET idProyecto = :idProyecto where id = :id")
    suspend fun modificarProyectoID(id: Int, idProyecto: Int)

    @Transaction
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) {
        val id = insertarProyecto(proyectoDTO.proyecto).toInt()

        proyectoDTO.etiquetas.forEach {
            insertarProyectoEtiqueta(ProyectoEtiqueta(id, it.id))
        }

        proyectoDTO.tareas.forEach {
            modificarProyectoID(it.id, id)
        }

    }

    @Transaction
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) {
        // Modificar proyecto
        modificarProyecto(proyectoDTO.proyecto)

        // Gestionar etiquetas
        eliminarRelacionProyectoEtiqueta(proyectoDTO.proyecto.id)
        proyectoDTO.etiquetas.forEach {
            insertarProyectoEtiqueta(ProyectoEtiqueta(proyectoDTO.proyecto.id, it.id))
        }

        // Gestionar tareas
        eliminarProyectoID(proyectoDTO.proyecto.id)
        proyectoDTO.tareas.forEach {
            modificarProyectoID(it.id, proyectoDTO.proyecto.id)
        }


    }
}