package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.OrdenarTareas
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import java.util.Date
import kotlin.apply

/**
 * ViewModel que une la aplicacion con la base de datos
 */
class TusTareasModel(application: Application): AndroidViewModel(application) {
    // Invocacion repositorio
    private val repository = TusTareasRepository(TusTareasDatabase.getDatabase( application))

    // Metodos de consulta de la base de datos
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = repository.obtenerTareasTerminanDiaEspecifico(fecha)
    fun obtenerTareasRetrasadas(fecha: Date) = repository.obtenerTareasRetrasadas(fecha)
    fun obtenerTareasProximas(fecha: Date) = repository.obtenerTareasProximas(fecha)
    fun obtenerEtiquetaPorID(id: Int) = repository.obtenerEtiquetaPorID(id)
    fun obtenerTodasLasTareas() = repository.obtenerTodasLasTareas()
    fun obtenerTareaDTOPorID(id: Int) = repository.obtenerTareaDTOPorID(id)


    // Filtros correctos evita duplicados de observers y aumenta la eficiencia y coherencia de la aplicación
    // Filtro para etiquetas
    private val textoEtiqueta = MutableLiveData("")
    fun actualizarTextoListadoEtiqueta(texto: String) {
        textoEtiqueta.value = texto
    }
    fun obtenerEtiquetasFiltradas() : LiveData<List<Etiqueta>> = textoEtiqueta.switchMap {
        texto ->
        repository.obtenerEtiquetasFiltradas(texto)
    }
    // Filtro para tareas
    private val prioridadTarea = MutableLiveData(Prioridad.entries.toTypedArray())
    fun actualizarPrioridadListadoTareas(prioridad: Array<Prioridad>) {
         prioridadTarea.value = prioridad
    }
    private val estadoTarea = MutableLiveData(Estado.entries.toTypedArray())
    fun actualizarEstadoListadoTareas(estado: Array<Estado>) {
        estadoTarea.value = estado
    }
    private val textoTarea = MutableLiveData("")
    fun actualizarTextoListadoTareas(texto: String) {
        textoTarea.value = texto
    }
    private val textoOrdenación = MutableLiveData(OrdenarTareas.FECHA_CREACION_ASC)
    fun actualizarTextoOrdenacionListadoTareas(nuevaOrdenacion: OrdenarTareas) {
        textoOrdenación.value = nuevaOrdenacion
    }


    // Necesario para 2 o más filtros sobre el mismo observer (query)
    // Cuidado con los parentesis son traicioneros
    private val vigiladorFiltrosTareas = MediatorLiveData<Unit>().apply {
        addSource(prioridadTarea) { value = Unit }
        addSource(estadoTarea) { value = Unit }
        addSource(textoTarea) { value = Unit }
        addSource(textoOrdenación) { value = Unit }
    }

    fun obtenerTareasFiltradas() : LiveData<List<Tarea>> = vigiladorFiltrosTareas.switchMap {
        repository.obtenerTareasFiltradas(
            prioridadTarea.value ?: Prioridad.entries.toTypedArray(),
            estadoTarea.value ?: Estado.entries.toTypedArray(),
            textoTarea.value ?: "",
            textoOrdenación.value ?: OrdenarTareas.FECHA_CREACION_ASC
        )

    }




    // Metodos de inserción en la base de datos
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.insertarEtiqueta(etiqueta)

    // Metodos de moficiación en la base de datos
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificarEtiqueta(etiqueta)
    suspend fun modificarTarea(tarea: Tarea) = repository.modificarTarea(tarea)


    // Metodos de eliminación en la base de datos
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = repository.eliminarEtiqueta(etiqueta)
}