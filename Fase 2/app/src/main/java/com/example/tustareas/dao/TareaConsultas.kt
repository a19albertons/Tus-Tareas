package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Tarea
import java.util.Date

@Dao
interface TareaConsultas {
    @Query("select * from tareas where fechaLimite = :fecha")
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date): LiveData<List<Tarea>>

}