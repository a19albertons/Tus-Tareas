package com.example.tustareas.modelView

import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que representa al submodelo de etiqueta detalles
 */
class EtiquetaDetallesModel(private val repository: TusTareasRepository) {
    // Metodo que obtiene una etiqueta por su id
    fun obtenerEtiquetaPorID(id: Int) = repository.etiquetaDetalles.obtenerEtiquetaPorID(id)

    // Metodos de eliminación en la base de datos
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = repository.etiquetaDetalles.eliminarEtiqueta(etiqueta)
}