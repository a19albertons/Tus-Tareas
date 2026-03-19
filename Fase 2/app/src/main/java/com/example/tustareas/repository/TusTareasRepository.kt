package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.OrdenarTareas
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import java.util.Date

class TusTareasRepository(database: TusTareasDatabase) {
    // Imporaction daos
    private val tareaConsultas = database.tareaConsultas()
    private val etiquetaConsultas = database.etiquetaConsultas()
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



}