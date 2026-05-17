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
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ProyectoDetallesConsultas {
    // Metodos que usan internamente solo aqui

    /**
     * Restablece en todas las tareas el id proyecto a null para el proyecto con el ID especificado. Solo usar en las transaciones
     *
     * @param id El ID del proyecto para el cual se restablecerá el ID del proyecto en las tareas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("UPDATE tareas SET idProyecto = null where idProyecto = :id")
    suspend fun eliminarProyectoID(id: Int)

    /**
     * Elimina un proyecto de la base de datos. Solo usar en las transaciones
     *
     * @param proyecto El proyecto a eliminar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Delete
    suspend fun eliminarProyecto(proyecto: Proyecto)

    /**
     * Obtiene un proyecto por su ID, incluyendo sus tareas y etiquetas relacionadas.
     *
     * @param id El ID del proyecto a obtener.
     * @return LiveData<ProyectoDTO> con el proyecto obtenido por su ID, incluyendo sus tareas y etiquetas relacionadas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Transaction
    @Query("SELECT * FROM proyectos where id = :id")
    fun obtenerProyectoPorId(id: Int): LiveData<ProyectoDTO>

    /**
     * Eliminar un proyecto junto con sus relaciones de tareas y etiquetas.
     *
     * @param proyecto El proyecto a eliminar junto con sus relaciones de tareas y etiquetas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Transaction
    suspend fun eliminarProyectoConTareaYEtiqueta(proyecto: Proyecto) {
        eliminarProyectoID(proyecto.id)
        eliminarProyecto(proyecto)
    }
}
