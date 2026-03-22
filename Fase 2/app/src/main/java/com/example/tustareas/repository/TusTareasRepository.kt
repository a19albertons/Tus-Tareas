package com.example.tustareas.repository

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
    private val proyectoConsultas = database.proyectoConsultas()
    private val proyectoModificaciones = database.proyectoModificaciones()
    private val etiquetaModificaciones = database.etiquetaModificaciones()
    private val tareaModificaciones = database.tareaModificaciones()




    // funcines daos
    // Tareas en tiempo no completas que terminen hoy
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = tareaConsultas.obtenerTareasTerminanDiaEspecifico(fecha)

    // Tareas no completadas y retrasadas
    fun obtenerTareasRetrasadas(fecha: Date) = tareaConsultas.obtenerTareasRetrasadas(fecha)

    // Tareas proximas
    fun obtenerTareasProximas(fecha: Date) = tareaConsultas.obtenerTareasProximas(fecha)

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
    // Filtrar tareas
    fun obtenerTareasFiltradas(prioridad: Array<Prioridad>, estado: Array<Estado>, textoTarea: String, orden: OrdenarTareas) =
        when (orden) {
            OrdenarTareas.FECHA_CREACION_ASC -> tareaConsultas.obtenerTareasFiltradasFechaCreacionAsc(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_CREACION_DES -> tareaConsultas.obtenerTareasFiltradasFechaCreacionDes(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_LIMITE_ASC -> tareaConsultas.obtenerTareasFiltradasFechaLimiteAsc(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_LIMITE_DES -> tareaConsultas.obtenerTareasFiltradasFechaLimiteDes(prioridad, estado, textoTarea)
        }

    // Obtener tarea dto por id
    fun obtenerTareaDTOPorID(id: Int) = tareaConsultas.obtenerTareaDTOPorID(id)

    // Obtener etiquetas restantes
    fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) = etiquetaConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })

    // Insertar nueva tarea
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = tareaModificaciones.insertarTareaConEtiqueta(tareaDTO)
    // Modifocar tarea con etiqueta
    suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) = tareaModificaciones.modificarTareaConEtiqueta(tareaDTO)
    // Borrar tarea
    suspend fun eliminarTarea(tarea: Tarea) = tareaModificaciones.eliminarTarea(tarea)
    // Consultar proyectos
    fun obtenerProyectosFiltradas(texto: String, inicio: OrdenarProyectosInicio, fin: OrdenarProyectoFin) =
        when (Pair(inicio, fin)) {
            Pair(OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FIN) -> proyectoConsultas.obtenerProyectosFiltradosPorDefecto(texto) // defecto
            Pair(OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FECHA_ASC) -> proyectoConsultas.obtenerProyectosFiltradosPorFinAsc(texto) // Fin ascendente
            Pair(OrdenarProyectosInicio.INICIO, OrdenarProyectoFin.FECHA_DES) -> proyectoConsultas.obtenerProyectosFiltradosPorFinDes(texto) // Fin desdendente
            Pair(OrdenarProyectosInicio.FECHA_ASC, OrdenarProyectoFin.FIN) -> proyectoConsultas.obtenerProyectosFiltradosPorInicioAsc(texto) // inicio ascendente
            Pair(OrdenarProyectosInicio.FECHA_ASC, OrdenarProyectoFin.FECHA_ASC) -> proyectoConsultas.obtenerProyectosFiltradosPorInicioYFinAsc(texto) // inicio y fin ascendente
            Pair(OrdenarProyectosInicio.FECHA_ASC, OrdenarProyectoFin.FECHA_DES) -> proyectoConsultas.obtenerProyectosFiltradosPorInicioAscYFinDes(texto) // inicio ascendente y fin descendente
            Pair(OrdenarProyectosInicio.FECHA_DES, OrdenarProyectoFin.FIN) -> proyectoConsultas.obtenerProyectosFiltradosPorInicioDes(texto) // inicio descendente
            Pair(OrdenarProyectosInicio.FECHA_DES, OrdenarProyectoFin.FECHA_ASC) -> proyectoConsultas.obtenerProyectosFiltradosPorInicioDesYFinAsc(texto) // inicio descendente y fin ascendente
            Pair(OrdenarProyectosInicio.FECHA_DES, OrdenarProyectoFin.FECHA_DES) -> proyectoConsultas.obtenerProyectosFiltradosPorInicioYFinDes(texto) // inicio y fin descendente

            else -> proyectoConsultas.obtenerProyectosFiltradosPorDefecto(texto)


        }

    // Obtener proyecto por id
    fun obtenerProyectoPorId(id: Int) = proyectoConsultas.obtenerProyectoPorId(id)

    // Obtener tareas restantes
    fun obtenerTareasRestantes(listaTareas: List<Tarea>) = tareaConsultas.obtenerTareasRestantes(listaTareas.map { it.id })
    // Inserta un proyecto nuevo con sus tareas y etiquetas
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = proyectoModificaciones.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    // Modificar proyecto existente
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = proyectoModificaciones.modificarProyectoConTareaYEtiqueta(proyectoDTO)
    // Eliminar proyecto
    suspend fun eliminarProyectoConTareaYEtiqueta(proyectoVisualizado: ProyectoDTO) = proyectoModificaciones.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado.proyecto)


}