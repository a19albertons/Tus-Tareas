package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.repository.ProyectoDetallesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Clase que gestiona el submodelo de proyecto detalles
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class ProyectoDetallesModel @Inject constructor(
    application: Application,
    private val repository: ProyectoDetallesRepository
) : AndroidViewModel(application) {
    /**
     * Obtiene un proyecto por su id
     *
     * @param id El ID del proyecto a obtener
     * @return El proyecto correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerProyectoPorId(id: Int) = repository.obtenerProyectoPorId(id)

    /**
     * Elimina un proyecto y sus relaciones (tareas y etiquetas)
     *
     * @param proyectoVisualizado El proyecto a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarProyectoConTareaYEtiqueta(proyectoVisualizado: ProyectoDTO) = repository.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado)

}