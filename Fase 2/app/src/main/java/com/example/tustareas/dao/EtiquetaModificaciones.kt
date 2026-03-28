package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update
import com.example.tustareas.modelos.Etiqueta

@Dao
interface EtiquetaModificaciones {

    @Insert
    suspend fun insertarEtiqueta(etiqueta: Etiqueta)

    @Update
    suspend fun modificarEtiqueta(etiqueta: Etiqueta)



}