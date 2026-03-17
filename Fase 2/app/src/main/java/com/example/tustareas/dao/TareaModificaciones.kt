package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.example.tustareas.modelos.Tarea

@Dao
interface TareaModificaciones {
    @Insert(onConflict = REPLACE)
    suspend fun modificarTarea(tarea: Tarea)
}