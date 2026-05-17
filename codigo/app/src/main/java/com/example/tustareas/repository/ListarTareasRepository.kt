package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import javax.inject.Inject

/**
 * Clase que va representar el subrepositorio que hara las consultas contra el dao de inicio
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ListarTareasRepository
    @Inject
    constructor(
        database: TusTareasDatabase,
    ) {
        private val listarTareasConsultas = database.listarTareasConsultas()

        /**
         * Obtiene las tareas filtradas por prioridad, estado y texto, ordenadas por el orden especificado
         *
         * @param prioridad El array de prioridades a filtrar
         * @param estado El array de estados a filtrar
         * @param textoTarea El texto a filtrar por el nombre de la tarea
         * @param orden El orden en el que se deben ordenar las tareas
         * @return Las tareas filtradas por prioridad, estado y texto, ordenadas por el orden especificado
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerTareasFiltradas(
            prioridad: Array<Prioridad>,
            estado: Array<Estado>,
            textoTarea: String,
            orden: OrdenarTareas,
        ) = when (orden) {
            OrdenarTareas.FECHA_CREACION_ASC -> listarTareasConsultas.obtenerTareasFiltradasFechaCreacionAsc(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_CREACION_DES -> listarTareasConsultas.obtenerTareasFiltradasFechaCreacionDes(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_LIMITE_ASC -> listarTareasConsultas.obtenerTareasFiltradasFechaLimiteAsc(prioridad, estado, textoTarea)
            OrdenarTareas.FECHA_LIMITE_DES -> listarTareasConsultas.obtenerTareasFiltradasFechaLimiteDes(prioridad, estado, textoTarea)
        }

        /**
         * Modifica una tarea en la base de datos
         *
         * @param tarea La tarea a modificar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        suspend fun modificarTarea(tarea: Tarea) = listarTareasConsultas.modificarTarea(tarea)
    }
