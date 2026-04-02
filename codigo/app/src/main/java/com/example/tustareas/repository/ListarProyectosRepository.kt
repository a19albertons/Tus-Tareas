package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio

/**
 * Clase que gestions los subrepositorios de listar proyectos
 */
class ListarProyectosRepository(database: TusTareasDatabase) {
    private val listarProyectosConsultas = database.listarProyectosConsultas()

    // Consultar proyectos
    fun obtenerProyectosFiltradas(texto: String, inicio: OrdenarProyectosInicio, fin: OrdenarProyectoFin) =
        when (Pair(inicio, fin)) {
            Pair(OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FIN) -> listarProyectosConsultas.obtenerProyectosFiltradosPorDefecto(texto) // defecto
            Pair(OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FECHA_ASC) -> listarProyectosConsultas.obtenerProyectosFiltradosPorFinAsc(texto) // Fin ascendente
            Pair(OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FECHA_DES) -> listarProyectosConsultas.obtenerProyectosFiltradosPorFinDes(texto) // Fin desdendente
            Pair(OrdenarProyectosInicio.FECHA_ASC, OrdenarProyectoFin.FIN) -> listarProyectosConsultas.obtenerProyectosFiltradosPorInicioAsc(texto) // inicio ascendente
            Pair(OrdenarProyectosInicio.FECHA_ASC, OrdenarProyectoFin.FECHA_ASC) -> listarProyectosConsultas.obtenerProyectosFiltradosPorInicioYFinAsc(texto) // inicio y fin ascendente
            Pair(OrdenarProyectosInicio.FECHA_ASC, OrdenarProyectoFin.FECHA_DES) -> listarProyectosConsultas.obtenerProyectosFiltradosPorInicioAscYFinDes(texto) // inicio ascendente y fin descendente
            Pair(OrdenarProyectosInicio.FECHA_DES, OrdenarProyectoFin.FIN) -> listarProyectosConsultas.obtenerProyectosFiltradosPorInicioDes(texto) // inicio descendente
            Pair(OrdenarProyectosInicio.FECHA_DES, OrdenarProyectoFin.FECHA_ASC) -> listarProyectosConsultas.obtenerProyectosFiltradosPorInicioDesYFinAsc(texto) // inicio descendente y fin ascendente
            Pair(OrdenarProyectosInicio.FECHA_DES, OrdenarProyectoFin.FECHA_DES) -> listarProyectosConsultas.obtenerProyectosFiltradosPorInicioYFinDes(texto) // inicio y fin descendente

            else -> listarProyectosConsultas.obtenerProyectosFiltradosPorDefecto(texto)


        }
}