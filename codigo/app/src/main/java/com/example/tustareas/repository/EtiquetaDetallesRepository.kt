package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Etiqueta
import javax.inject.Inject

/**
 * Clase que represente el subrepositorio de detalles de etiquetas
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class EtiquetaDetallesRepository @Inject constructor(
    database: TusTareasDatabase
) {
    private val etiquetaDetallesConsultas = database.etiquetaDetallesConsultas()

    /**
     * Obtiene una etiqueta por su id
     *
     * @param id El ID de la etiqueta a obtener
     * @return La etiqueta correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerEtiquetaPorID(id: Int) = etiquetaDetallesConsultas.obtenerEtiquetaPorID(id)

    /**
     * Elimina una etiqueta de la base de datos
     *
     * @param etiqueta La etiqueta a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = etiquetaDetallesConsultas.eliminarEtiqueta(etiqueta)
}