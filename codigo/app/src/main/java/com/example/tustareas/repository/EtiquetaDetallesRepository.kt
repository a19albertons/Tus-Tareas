package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que represente el subrepositorio de detalles de etiquetas
 */
class EtiquetaDetallesRepository(database: TusTareasDatabase) {
    private val etiquetaDetallesConsultas = database.etiquetaDetallesConsultas()
    // Etiqueta por id
    fun obtenerEtiquetaPorID(id: Int) = etiquetaDetallesConsultas.obtenerEtiquetaPorID(id)

    // Eliminar etiqueta existente
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = etiquetaDetallesConsultas.eliminarEtiqueta(etiqueta)
}