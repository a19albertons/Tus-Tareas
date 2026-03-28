package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Proyecto

@Dao
interface ProyectoConsultas {



    @Transaction
    @Query("SELECT * FROM proyectos where id = :id")
    fun obtenerProyectoPorId(id: Int): LiveData<ProyectoDTO>





}