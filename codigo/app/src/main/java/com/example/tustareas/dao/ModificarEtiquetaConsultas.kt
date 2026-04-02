package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa las consultas contra la bd en modificar etiqueta
 */
@Dao
interface ModificarEtiquetaConsultas {
    // Inserta una etiqueta
    @Insert
    suspend fun insertarEtiqueta(etiqueta: Etiqueta)

    // Modifica una etiqueta
    @Update
    suspend fun modificarEtiqueta(etiqueta: Etiqueta)
}