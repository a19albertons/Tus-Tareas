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
    @Insert(onConflict = REPLACE)
    suspend fun insertarProyectoEtiqueta(proyectoEtiqueta: ProyectoEtiqueta)
    @Query("DELETE FROM ProyectoEtiquetas WHERE idProyecto = :id")
    suspend fun eliminarRelacionProyectoEtiqueta(id: Int)
    @Query("UPDATE tareas SET idProyecto = null where id in (:ids)")
    suspend fun eliminarProyectoID(ids: List<Int>)
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
}