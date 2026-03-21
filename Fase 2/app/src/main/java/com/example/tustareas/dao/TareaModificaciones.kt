package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta

@Dao
interface TareaModificaciones {
    // Usar solo aqui porque devuelve el id interno para transaction
    @Insert
    suspend fun insertarTarea(tarea: Tarea) : Long
    @Update
    suspend fun modificarTarea(tarea: Tarea)

    // Usar solo aqui porque se us para la inserción de las relacion tarea etiqueta internamente
    @Insert
    suspend fun insertarTareaEtiqueta(tareaEtiqueta: TareaEtiqueta)
    // Usar solo aqui para eliminar las relaciones preexistentes y crear las nuevas
    @Query("DELETE FROM TareaEtiquetas WHERE idTarea = :id")
    suspend fun eliminarRelacionesTarea(id: Int)
    // Debería hacer un borrado recursivo en cascada
    @Delete
    suspend fun eliminarTarea(tarea: Tarea)



    @Transaction
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) {
        val id = insertarTarea(tareaDTO.tarea).toInt()
        tareaDTO.etiquetas.forEach {
            etiqueta ->
            insertarTareaEtiqueta(TareaEtiqueta(id, etiqueta.id))
        }
    }
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