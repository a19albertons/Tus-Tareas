package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import java.util.Date

@Dao
interface TareaConsultas {


    @Transaction
    @Query("select * from tareas")
    fun obtenerTodasLasTareas(): LiveData<List<Tarea>>










}