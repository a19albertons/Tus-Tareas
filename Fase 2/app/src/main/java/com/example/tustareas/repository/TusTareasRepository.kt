package com.example.tustareas.repository

import com.example.tustareas.dao.EtiquetaConsultas
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import java.util.Date

class TusTareasRepository(database: TusTareasDatabase) {
    // Imporaction daos
    private val tareaConsultas = database.tareaConsultas()
    private val etiquetaConsultas = database.etiquetaConsultas()
    private val proyectoModificaciones = database.proyectoModificaciones()
    private val etiquetaModificaciones = database.etiquetaModificaciones()
    private val tareaModificaciones = database.tareaModificaciones()

    val inicio = InicioRepository(database)
    val verMas = VerMasRepository(database)
    val listarTareas = ListarTareasRepository(database)
    val tareaDetalles = TareaDetallesRepository(database)
    val modificarTareasModel = ModificarTareasRepository(database)
    val listarProyectosModel = ListarProyectosRepository(database)
    val proyectoDetallesModel = ProyectoDetallesRepository(database)




    // funcines daos


    // Etiquetas filtradas
    fun obtenerEtiquetasFiltradas(texto: String) = etiquetaConsultas.obtenerEtiquetasFiltradas(texto)
    // Etiqueta por id
    fun obtenerEtiquetaPorID(id: Int) = etiquetaConsultas.obtenerEtiquetaPorID(id)

    // Insertar nueva etiqueta
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = etiquetaModificaciones.insertarEtiqueta(etiqueta)
    // Modificar etiqueta existente
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = etiquetaModificaciones.modificarEtiqueta(etiqueta)
    // Eliminar etiqueta existente
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = etiquetaModificaciones.eliminarEtiqueta(etiqueta)

    // Modificar tarea existente
    suspend fun modificarTarea(tarea: Tarea) = tareaModificaciones.modificarTarea(tarea)

    // Obtener todas las tareas
    fun obtenerTodasLasTareas() = tareaConsultas.obtenerTodasLasTareas()












    // Obtener tareas restantes
    fun obtenerTareasRestantes(listaTareas: List<Tarea>) = tareaConsultas.obtenerTareasRestantes(listaTareas.map { it.id })
    // Inserta un proyecto nuevo con sus tareas y etiquetas
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = proyectoModificaciones.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    // Modificar proyecto existente
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = proyectoModificaciones.modificarProyectoConTareaYEtiqueta(proyectoDTO)

    suspend fun limpiarTareasCompletas() = tareaModificaciones.limpiarTareasCompletas()
    // Tareas completadas por dia
    fun tareasCompletadasPorDia(i: Long) = tareaConsultas.tareasCompletadasPorDia(i)
    // Tareas no completadas por dia
    fun tareasNoCompletadasPorDia(i: Long) = tareaConsultas.tareasNoCompletadasPorDia(i)
    // Obtener todas las tareas completadas
    fun obtenerCantidadTareasCompletas() = tareaConsultas.obtenerCantidadTareasCompletas()
    // Obtener todas las tareas pendientes
    fun obtenerCantidadTareasPendientes() = tareaConsultas.obtenerCantidadTareasPendientes()
    // Obtener todas las tareas retrasadas
    fun obtenerCantidadTareasRetrasadas() = tareaConsultas.obtenerCantidadTareasRetrasadas()

    // Obtener tareas completas entre 2 fechas
    fun obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio: Long, fechaFin: Long) = tareaConsultas.obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio, fechaFin)

    // Obtener tareas pendientes entre 2 fechas
    fun obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio: Long, fechaFin: Long) = tareaConsultas.obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio, fechaFin)





    // Legacy

    // Obtener etiquetas restantes
    fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) = etiquetaConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })
}