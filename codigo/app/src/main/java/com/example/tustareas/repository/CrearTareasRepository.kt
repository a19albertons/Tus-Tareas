package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Etiqueta
import javax.inject.Inject

/**
 * Clase que gestiona el subrepositorio de crear tareas
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class CrearTareasRepository
    @Inject
    constructor(
        database: TusTareasDatabase,
    ) {
        private val crearTareaConsultas = database.crearTareaConsultas()

        /**
         * Obtiene las etiquetas restantes de una tarea que no estan usadas en la tarea
         *
         * @param listaEtiquetas La lista de etiquetas que ya estan usadas en la tarea
         * @return Las etiquetas restantes de una tarea que no estan usadas en la tarea
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerEtiquetasRestantes(listaEtiquetas: List<Etiqueta>) =
            crearTareaConsultas.obtenerEtiquetasRestantes(listaEtiquetas.map { it.id })

        /**
         * Inserta una nueva tarea en la base de datos con sus etiquetas
         *
         * @param tareaDTO El DTO de la tarea a insertar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = crearTareaConsultas.insertarTareaConEtiqueta(tareaDTO)
    }
