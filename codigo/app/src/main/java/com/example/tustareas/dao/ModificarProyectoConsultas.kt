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

/**
 * Clase que representa las consultas contra la bd en modificar proyecto
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ModificarProyectoConsultas {
    /**
     * Inserta un proyecto. Solo usar en las transaciones
     *
     * @param proyecto El proyecto a insertar.
     * @return Long con el ID del proyecto insertado.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Insert
    suspend fun insertarProyecto(proyecto: Proyecto): Long

    /**
     * Modifica un proyecto. Solo usar en las transaciones
     *
     * @param proyecto El proyecto a modificar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Update
    suspend fun modificarProyecto(proyecto: Proyecto)

    /**
     * Inserta una relación proyecto-etiqueta. Solo usar en las transaciones
     *
     * @param proyectoEtiqueta La relación proyecto-etiqueta a insertar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Insert
    suspend fun insertarProyectoEtiqueta(proyectoEtiqueta: ProyectoEtiqueta)

    /**
     * Actualiza el id del proyecto en unta tarea. Solo usar en las transaciones
     *
     * @param id El ID de la tarea a modificar.
     * @param idProyecto El ID del proyecto a asignar a la tarea.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("UPDATE tareas SET idProyecto = :idProyecto where id = :id")
    suspend fun modificarProyectoID(
        id: Int,
        idProyecto: Int,
    )

    /**
     * Borra la relación entre un proyecto y sus etiquetas. Solo usar en las transaciones
     * @param id El ID del proyecto cuyas etiquetas se quieren eliminar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("DELETE FROM ProyectoEtiquetas WHERE idProyecto = :id")
    suspend fun eliminarRelacionProyectoEtiqueta(id: Int)
    // Borra el id de proyecto en una tarea

    /**
     * Borra el id de proyecto en una tarea. Solo usar en las transaciones
     *
     * @param id El ID del proyecto cuyas tareas se quieren eliminar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("UPDATE tareas SET idProyecto = null where idProyecto = :id")
    suspend fun eliminarProyectoID(id: Int)

    /**
     * Obtiene las tareas que no tienen asignado un proyecto
     *
     * @param lista La lista de IDs de tareas que ya están asignadas al proyecto.
     * @return LiveData<List<Tarea>> con las tareas que no tienen asignado un proyecto y que no están en la lista de IDs proporcionada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from tareas where id not in (:lista) AND (idProyecto is null OR idProyecto = :idProyecto)")
    fun obtenerTareasRestantes(
        lista: List<Int>,
        idProyecto: Int,
    ): LiveData<List<Tarea>>

    /**
     * Obtiene las etiquetas que no están asignadas al proyecto
     *
     * @param lista La lista de IDs de etiquetas que ya están asignadas al proyecto.
     * @return LiveData<List<Etiqueta>> con las etiquetas que no estan asignadas al proyecto y que no están en la lista de IDs proporcionada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from etiquetas where id not in (:lista)")
    fun obtenerEtiquetasRestantes(lista: List<Int>): LiveData<List<Etiqueta>>

    /**
     * Modifica un proyecto existente junto con sus tareas y etiquetas asociadas.
     *
     * @param proyectoDTO El objeto ProyectoDTO que contiene el proyecto, las tareas y las etiquetas a modificar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
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
