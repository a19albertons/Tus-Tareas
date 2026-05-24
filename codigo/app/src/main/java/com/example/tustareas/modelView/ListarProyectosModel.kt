package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.repository.ListarProyectosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Clase que gestiona el submodelo de listar proyectos.
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de listar proyectos
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class ListarProyectosModel
    @Inject
    constructor(
        application: Application,
        private val repository: ListarProyectosRepository,
    ) : AndroidViewModel(application) {
        // Filtro para proyectos
        // Valores iniciales filtros
        private val textoProyecto = MutableLiveData("")
        private val inicioProyecto = MutableLiveData(OrdenarProyectosInicio.INICIO)
        private val finProyecto = MutableLiveData(OrdenarProyectoFin.FIN)

        /**
         * Actualiza el valor del filtro de texto para los proyectos
         *
         * @param texto El nuevo valor del filtro de texto para los proyectos
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarTextoListadoProyectos(texto: String) {
            textoProyecto.value = texto
        }

        /**
         * Actualiza el valor del filtro de ordenación por fecha de inicio para los proyectos
         *
         * @param inicio El nuevo valor del filtro de ordenación por fecha de inicio para los proyectos
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarInicioProyecto(inicio: OrdenarProyectosInicio) {
            inicioProyecto.value = inicio
        }

        /**
         * Actualiza el valor del filtro de ordenación por fecha de fin para los proyectos
         *
         * @param fin El nuevo valor del filtro de ordenación por fecha de fin para los proyectos
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun actualizarFinProyecto(fin: OrdenarProyectoFin) {
            finProyecto.value = fin
        }

        // Vigila cualquier cambio en los filtros
        private val vigiladorFiltrosProyectos =
            MediatorLiveData<Unit>().apply {
                addSource(textoProyecto) { value = Unit }
                addSource(inicioProyecto) { value = Unit }
                addSource(finProyecto) { value = Unit }
            }

        /**
         * Obtiene la lista de proyectos filtrados según los filtros de texto, ordenación por fecha de inicio y ordenación por fecha de fin
         *
         * @return Un LiveData que contiene una lista de proyectos filtrados según los filtros de texto,
         * ordenación por fecha de inicio y ordenación por fecha de fin
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerProyectosFiltradas(): LiveData<List<Proyecto>> =
            vigiladorFiltrosProyectos.switchMap {
                repository.obtenerProyectosFiltradas(
                    textoProyecto.value!!,
                    inicioProyecto.value!!,
                    finProyecto.value!!,
                )
            }
    }
