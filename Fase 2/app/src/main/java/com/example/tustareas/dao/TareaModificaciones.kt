package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta

@Dao
interface TareaModificaciones {




    // Legacy
    @Update
    suspend fun modificarTarea(tarea: Tarea)

    @Query("select * from etiquetas where id not in (:lista)")
    fun obtenerEtiquetasRestantes(lista : List<Int>): LiveData<List<Etiqueta>>







    @Query("DELETE FROM tareas WHERE estado = :estado")
    suspend fun limpiarTareasCompletas(estado: Estado = Estado.Completada)
}