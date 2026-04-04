package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa las consultas en listar etiquetas
 */
@Dao
interface ListarEtiquetasConsultas {
    // Obtener todas las etiquetas en base al filtro de texto
    @Query("select * from etiquetas where LOWER(nombre) like LOWER('%' || :texto || '%') OR LOWER(descripcion) like LOWER('%' || :texto || '%')")
    fun obtenerEtiquetasFiltradas(texto: String): LiveData<List<Etiqueta>>
}