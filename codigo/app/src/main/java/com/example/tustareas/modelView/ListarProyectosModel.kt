package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.repository.TusTareasRepository

/**
 * Clase que gestiona el submodelo de listar proyectos
 */
class ListarProyectosModel(private val repository: TusTareasRepository) {
    // Filtro para proyectos
    // Valores iniciales filtros
    private val textoProyecto = MutableLiveData("")
    private val inicioProyecto = MutableLiveData(OrdenarProyectosInicio.INICIO)
    private val finProyecto = MutableLiveData(OrdenarProyectoFin.FIN)
    // Actualización valores filtros
    fun actualizarTextoListadoProyectos(texto: String) {
        textoProyecto.value = texto
    }
    fun actualizarInicioProyecto(inicio: OrdenarProyectosInicio) {
        inicioProyecto.value = inicio
    }
    fun actualizarFinProyecto(fin: OrdenarProyectoFin) {
        finProyecto.value = fin
    }

    // Vigila cualquier cambio en los filtros
    private val vigiladorFiltrosProyectos = MediatorLiveData<Unit>().apply {
        addSource(textoProyecto) { value = Unit }
        addSource(inicioProyecto) { value = Unit }
        addSource(finProyecto) { value = Unit }
    }
    // Aplica el cambio en los filtros
    fun obtenerProyectosFiltradas() : LiveData<List<Proyecto>> = vigiladorFiltrosProyectos.switchMap {
        repository.listarProyectos.obtenerProyectosFiltradas(
            textoProyecto.value!!,
            inicioProyecto.value!!,
            finProyecto.value!!
        )
    }
}