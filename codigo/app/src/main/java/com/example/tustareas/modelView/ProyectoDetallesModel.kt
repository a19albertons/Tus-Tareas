package com.example.tustareas.modelView

import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de proyecto detalles
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class ProyectoDetallesModel(private val repository: TusTareasRepository) {
    /**
     * Obtiene un proyecto por su id
     *
     * @param id El ID del proyecto a obtener
     * @return El proyecto correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerProyectoPorId(id: Int) = repository.proyectoDetalles.obtenerProyectoPorId(id)

    /**
     * Elimina un proyecto y sus relaciones (tareas y etiquetas)
     *
     * @param proyectoVisualizado El proyecto a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarProyectoConTareaYEtiqueta(proyectoVisualizado: ProyectoDTO) = repository.proyectoDetalles.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado)

}