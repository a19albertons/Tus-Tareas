package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Transaction
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta

@Dao
interface TareaModificaciones {
    // Usar solo aqui porque devuelve el id interno para transaction
    @Insert
    suspend fun insertarTarea(tarea: Tarea) : Long
    @Insert(onConflict = REPLACE)
    suspend fun modificarTarea(tarea: Tarea)

    // Usar solo aqui porque se us para la inserción de las relacion tarea etiqueta internamente
    @Insert(onConflict = REPLACE)
    suspend fun insertarTareaEtiqueta(tareaEtiqueta: TareaEtiqueta)


    @Transaction
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) {
        val id = insertarTarea(tareaDTO.tarea).toInt()
        tareaDTO.etiquetas.forEach {
            etiqueta ->
            insertarTareaEtiqueta(TareaEtiqueta(id, etiqueta.id))
        }
    }
}