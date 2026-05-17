package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa las consultas contra la bd en etiqueta detalles
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface EtiquetaDetallesConsultas {
    /**
     * Obtiene una etiqueta por su id
     *
     * @param id El ID de la etiqueta a obtener.
     * @return LiveData<Etiqueta> devuelve la etiqueta obtenida por su ID.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from etiquetas where id = :id")
    fun obtenerEtiquetaPorID(id: Int): LiveData<Etiqueta>

    // Elimina una etiqueta

    /**
     * Elimina una etiqueta
     *
     * @param etiqueta La etiqueta a eliminar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Delete
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta)
}
