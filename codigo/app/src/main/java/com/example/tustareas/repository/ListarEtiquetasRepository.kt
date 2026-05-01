package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase

/**
 * Clase que representa el subrepositorio de listar etiquetas
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ListarEtiquetasRepository(database: TusTareasDatabase) {
    private val listarEtiquetasConsultas = database.listarEtiquetasConsultas()

    /**
     * Obtiene las etiquetas filtradas por un texto
     *
     * @param texto El texto a filtrar
     * @return Las etiquetas filtradas por el texto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerEtiquetasFiltradas(texto: String) = listarEtiquetasConsultas.obtenerEtiquetasFiltradas(texto)
}