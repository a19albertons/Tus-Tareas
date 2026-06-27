package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa las consultas contra la bd en crear etiqueta
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface CrearEtiquetaConsultas {
    /**
     * Inserta una etiqueta
     *
     * @param etiqueta La etiqueta a insertar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Insert
    suspend fun insertarEtiqueta(etiqueta: Etiqueta)
}
