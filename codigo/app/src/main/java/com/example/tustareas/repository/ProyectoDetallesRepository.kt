package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO

/**
 * Clase que gestiona el subrepositorio de proyecto detalles
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ProyectoDetallesRepository(database: TusTareasDatabase) {
    private val proyectoDetallesConsultas = database.proyectoDetallesConsultas()

    /**
     * Obtiene un proyecto por su id
     *
     * @param id El ID del proyecto a obtener
     * @return El proyecto correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerProyectoPorId(id: Int) = proyectoDetallesConsultas.obtenerProyectoPorId(id)

    /**
     * Elimina un proyecto de la base de datos junto con sus tareas y etiquetas
     * @param proyectoVisualizado El proyecto a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarProyectoConTareaYEtiqueta(proyectoVisualizado: ProyectoDTO) = proyectoDetallesConsultas.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado.proyecto)
}