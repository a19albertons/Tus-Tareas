package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Etiqueta

/**
 * Clase que representa al subrepositorio de modificar etiquetas
 */
class ModificarEtiquetasRepository(database: TusTareasDatabase) {
    private val modificarEtiquetaConsultas = database.modificarEtiquetaConsultas()
    // Insertar nueva etiqueta
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = modificarEtiquetaConsultas.insertarEtiqueta(etiqueta)
    // Modificar etiqueta existente
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = modificarEtiquetaConsultas.modificarEtiqueta(etiqueta)
}