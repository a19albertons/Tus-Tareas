package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa las consultas en listar etiquetas
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ListarEtiquetasConsultas {
    /**
     * Obtiene todas las etiquetas en base al filtro de texto
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Etiqueta>> devuelve una lista de etiquetas que cumplen con el filtro
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from etiquetas where LOWER(nombre) like LOWER('%' || :texto || '%') OR LOWER(descripcion) like LOWER('%' || :texto || '%')")
    fun obtenerEtiquetasFiltradas(texto: String): LiveData<List<Etiqueta>>
}