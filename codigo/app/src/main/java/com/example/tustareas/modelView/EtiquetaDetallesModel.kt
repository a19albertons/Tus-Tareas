package com.example.tustareas.modelView

import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que representa al submodelo de etiqueta detalles
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class EtiquetaDetallesModel(private val repository: TusTareasRepository) {
    /**
     * Obtiene una etiqueta por su id
     *
     * @param id El ID de la etiqueta a obtener
     * @return La etiqueta correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerEtiquetaPorID(id: Int) = repository.etiquetaDetalles.obtenerEtiquetaPorID(id)

    /**
     * Elimina una etiqueta de la base de datos
     *
     * @param etiqueta La etiqueta a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = repository.etiquetaDetalles.eliminarEtiqueta(etiqueta)
}