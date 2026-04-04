package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.ProyectoEtiqueta
import com.example.tustareas.modelos.Tarea

@Dao
interface ModificarProyectoConsultas {
    // Solo usar aqui los seis primeros metodos son para los transaction
    // Inserta un proyecto
    @Insert
    suspend fun insertarProyecto(proyecto: Proyecto) : Long
    // Modifica un proyecto
    @Update
    suspend fun modificarProyecto(proyecto: Proyecto)
    // Inserta una relacion proyecto-etiqueta
    @Insert
    suspend fun insertarProyectoEtiqueta(proyectoEtiqueta: ProyectoEtiqueta)
    // Actualiza el id de proyecto en una tarea
    @Query("UPDATE tareas SET idProyecto = :idProyecto where id = :id")
    suspend fun modificarProyectoID(id: Int, idProyecto: Int)
    // Borra en la relacion N:N donde corresponda
    @Query("DELETE FROM ProyectoEtiquetas WHERE idProyecto = :id")
    suspend fun eliminarRelacionProyectoEtiqueta(id: Int)
    // Borra el id de proyecto en una tarea
    @Query("UPDATE tareas SET idProyecto = null where idProyecto = :id")
    suspend fun eliminarProyectoID(id: Int)


    // Obtiene las tareas libres
    @Query("select * from tareas where id not in (:lista)")
    fun obtenerTareasRestantes(lista : List<Int>): LiveData<List<Tarea>>

    // Obtiene las etiquetas libres (no usadas por el propio tarea/proyecto
    @Query("select * from etiquetas where id not in (:lista)")
    fun obtenerEtiquetasRestantes(lista : List<Int>): LiveData<List<Etiqueta>>



    // Inserción de un novo proxecto
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

    // Modificar proyecto existente
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