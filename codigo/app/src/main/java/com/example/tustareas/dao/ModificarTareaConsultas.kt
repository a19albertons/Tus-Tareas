package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta

/**
 * Clase que gestiona todas las consultas contra la base de datos
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ModificarTareaConsultas {
    // Usar solo aqui porque devuelve el id interno para transaction
    /**
     * Inserta una tarea y devuelve su id. Solo usar en las transaciones
     *
     * @param tarea La tarea a insertar.
     * @return Long con el ID de la tarea insertada.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Insert
    suspend fun insertarTarea(tarea: Tarea) : Long

    /**
     * Modifica una tarea. Solo usar en las transaciones
     *
     * @param tarea La tarea a modificar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Update
    suspend fun modificarTarea(tarea: Tarea)

    /**
     * Inserta una relación tarea-etiqueta. Solo usar en las transaciones
     *
     * @param tareaEtiqueta La relación tarea-etiqueta a insertar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Insert
    suspend fun insertarTareaEtiqueta(tareaEtiqueta: TareaEtiqueta)

    /**
     * Elimina las relaciones tarea-etiqueta de una tarea. Solo usar en las transaciones
     *
     * @param id El ID de la tarea a eliminar sus relaciones.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("DELETE FROM TareaEtiquetas WHERE idTarea = :id")
    suspend fun eliminarRelacionesTarea(id: Int)

    /**
     * Obtiene las etiquetas restantes que no están asociadas a la tarea.
     *
     * @param lista La lista de IDs de las etiquetas asociadas a la tarea.
     * @return LiveData<List<Etiqueta>> devuelve una lista de etiquetas que no están asociadas a la tarea.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from etiquetas where id not in (:lista)")
    fun obtenerEtiquetasRestantes(lista : List<Int>): LiveData<List<Etiqueta>>

    /**
     * Inserta la tarea junto con sus etiquetas.
     *
     * @param tareaDTO El objeto TareaDTO que contiene la tarea y sus etiquetas a insertar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Transaction
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) {
        val id = insertarTarea(tareaDTO.tarea).toInt()
        tareaDTO.etiquetas.forEach {
                etiqueta ->
            insertarTareaEtiqueta(TareaEtiqueta(id, etiqueta.id))
        }
    }

    /**
     * Modifica la tarea junto con sus etiquetas.
     *
     * @param tareaDTO El objeto TareaDTO que contiene la tarea y sus etiquetas a modificar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Transaction
    suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) {
        modificarTarea(tareaDTO.tarea)
        // Eliminar relaciones anteriores
        eliminarRelacionesTarea(tareaDTO.tarea.id)
        // Insertar nuevas relaciones
        tareaDTO.etiquetas.forEach {
                etiqueta ->
            insertarTareaEtiqueta(TareaEtiqueta(tareaDTO.tarea.id, etiqueta.id))
        }
    }
}