package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase

/**
 * Clase que representa el subrepositorio de listar etiquetas
 */
class ListarEtiquetasRepository(database: TusTareasDatabase) {
    private val listarEtiquetasConsultas = database.listarEtiquetasConsultas()
    // Etiquetas filtradas
    fun obtenerEtiquetasFiltradas(texto: String) = listarEtiquetasConsultas.obtenerEtiquetasFiltradas(texto)
}