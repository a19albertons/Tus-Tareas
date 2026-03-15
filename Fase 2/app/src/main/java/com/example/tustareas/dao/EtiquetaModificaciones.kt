package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.tustareas.modelos.Etiqueta

@Dao
interface EtiquetaModificaciones {

    @Insert
    suspend fun insertarEtiqueta(etiqueta: Etiqueta)

}