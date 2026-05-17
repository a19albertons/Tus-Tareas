package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TareaDetallesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Clase que gestiona el submodelo de tarea detalles
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de tarea detalles
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class TareaDetallesModel
    @Inject
    constructor(
        application: Application,
        private val repository: TareaDetallesRepository,
    ) : AndroidViewModel(application) {
        /**
         * Obtiene una tarea DTO por su id
         *
         * @param id El ID de la tarea a obtener
         * @return La tarea DTO correspondiente. Debería existir
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerTareaDTOPorID(id: Int) = repository.obtenerTareaDTOPorID(id)

        /**
         * Elimina una tarea de la base de datos
         *
         * @param tarea La tarea a eliminar
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        suspend fun eliminarTarea(tarea: Tarea) = repository.eliminarTarea(tarea)
    }
