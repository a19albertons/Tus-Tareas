package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Etiqueta
import javax.inject.Inject

/**
 * Clase que representa al subrepositorio de crear etiquetas
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class CrearEtiquetasRepository
    @Inject
    constructor(
        private val database: TusTareasDatabase,
    ) {
        private val modificarEtiquetaConsultas = database.crearEtiquetaConsultas()

        /**
         * Inserta una nueva etiqueta en la base de datos
         *
         * @param etiqueta La etiqueta a insertar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        suspend fun insertarEtiqueta(etiqueta: Etiqueta) = modificarEtiquetaConsultas.insertarEtiqueta(etiqueta)
    }
