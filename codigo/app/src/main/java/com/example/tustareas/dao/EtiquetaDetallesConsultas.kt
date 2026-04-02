package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa las consultas contra la bd en etiqueta detalles
 */
@Dao
interface EtiquetaDetallesConsultas {
    // Obtiene una etiqueta por su id
    @Query("select * from etiquetas where id = :id")
    fun obtenerEtiquetaPorID(id: Int): LiveData<Etiqueta>

    // Elimina una etiqueta
    @Delete
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta)
}