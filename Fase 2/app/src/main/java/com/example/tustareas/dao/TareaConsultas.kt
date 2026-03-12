package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.modelos.Tarea
import java.util.Date

@Dao
interface TareaConsultas {
    @Transaction
    @Query("select * from tareas where fechaLimite = :fecha AND estado = 'EnTiempo'")
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date): LiveData<List<Tarea>>

    @Transaction
    @Query("select * from tareas where fechaLimite < :fecha AND (estado = 'EnTiempo' OR estado = 'Retrasada')")
    fun obtenerTareasRetrasadas(fecha: Date): LiveData<List<Tarea>>
}