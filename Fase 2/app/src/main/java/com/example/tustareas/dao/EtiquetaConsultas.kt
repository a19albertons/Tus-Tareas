package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Etiqueta

@Dao
interface EtiquetaConsultas {


    @Query("select * from etiquetas where id = :id")
    fun obtenerEtiquetaPorID(id: Int): LiveData<Etiqueta>


}