package com.example.tustareas.modelView

import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que representa al submodelo de modificar etiqueta.
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ModificarEtiquetasModel(private val repository: TusTareasRepository) {

    /**
     * Inserta una nueva etiqueta en la base de datos
     *
     * @param etiqueta La etiqueta a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.modificacionEtiqueta.insertarEtiqueta(etiqueta)

    /**
     * Modifica una etiqueta en la base de datos
     *
     * @param etiqueta La etiqueta a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificacionEtiqueta.modificarEtiqueta(etiqueta)
}