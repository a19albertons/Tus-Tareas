package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa al subrepositorio de modificar etiquetas
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ModificarEtiquetasRepository(database: TusTareasDatabase) {
    private val modificarEtiquetaConsultas = database.modificarEtiquetaConsultas()

    /**
     * Inserta una nueva etiqueta en la base de datos
     *
     * @param etiqueta La etiqueta a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = modificarEtiquetaConsultas.insertarEtiqueta(etiqueta)

    /**
     * Modifica una etiqueta existente en la base de datos
     *
     * @param etiqueta La etiqueta a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = modificarEtiquetaConsultas.modificarEtiqueta(etiqueta)
}