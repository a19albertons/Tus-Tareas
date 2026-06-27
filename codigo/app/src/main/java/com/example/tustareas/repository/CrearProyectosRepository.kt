package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import javax.inject.Inject

/**
 * Clase que representa al subrepositorio de crear proyectos
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class CrearProyectosRepository
    @Inject
    constructor(
        private val database: TusTareasDatabase,
    ) {
        private val crearProyectoConsultas = database.crearProyectoConsultas()

        /**
         * Obtiene las tareas restantes de un proyecto que no esten usadas por otros proyectos  y estan deseleccionada por el actual
         *
         * @param listaTareas La lista de tareas a comparar
         * @param idProyecto El ID del proyecto a comparar
         * @return Las tareas restantes de un proyecto que no esten usadas por otros proyectos y estan deseleccionada por el actual
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerTareasRestantes(
            listaTareas: List<Tarea>,
            idProyecto: Int,
        ) = crearProyectoConsultas.obtenerTareasRestantes(listaTareas.map { it.id }, idProyecto)

        /**
         * Obtiene las etiquetas restantes de un proyecto que no esten usadas
         *
         * @param listaEtiquetas La lista de etiquetas a comparar
         * @return Las etiquetas restantes de un proyecto que no esten usadas
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) =
            crearProyectoConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })

        /**
         * Inserta un proyecto nuevo con sus tareas y etiquetas
         *
         * @param proyectoDTO El DTO del proyecto a insertar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) =
            crearProyectoConsultas.insertarProyectoConTareaYEtiqueta(proyectoDTO)
    }
