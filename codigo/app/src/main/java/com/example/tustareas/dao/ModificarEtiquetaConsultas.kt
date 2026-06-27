package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Update
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa las consultas contra la bd en modificar etiqueta
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ModificarEtiquetaConsultas {
    /**
     * Modifica una etiqueta
     *
     * @param etiqueta La etiqueta a modificar.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Update
    suspend fun modificarEtiqueta(etiqueta: Etiqueta)
}
