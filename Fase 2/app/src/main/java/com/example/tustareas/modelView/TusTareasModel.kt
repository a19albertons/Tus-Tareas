package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.filtros.OrdenarTareas
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import com.github.mikephil.charting.data.BarEntry
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
    fun obtenerProyectoPorId(id: Int) = repository.obtenerProyectoPorId(id)





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
    // Filtro para proyectos
    private val textoProyecto = MutableLiveData("")
    private val inicioProyecto = MutableLiveData(OrdenarProyectosInicio.INICIO)
    private val finProyecto = MutableLiveData(OrdenarProyectoFin.FIN)
    fun actualizarTextoListadoProyectos(texto: String) {
        textoProyecto.value = texto
    }
    fun actualizarInicioProyecto(inicio: OrdenarProyectosInicio) {
        inicioProyecto.value = inicio
    }
    fun actualizarFinProyecto(fin: OrdenarProyectoFin) {
        finProyecto.value = fin
    }

    private val vigiladorFiltrosProyectos = MediatorLiveData<Unit>().apply {
        addSource(textoProyecto) { value = Unit }
        addSource(inicioProyecto) { value = Unit }
        addSource(finProyecto) { value = Unit }
    }
    fun obtenerProyectosFiltradas() : LiveData<List<Proyecto>> = vigiladorFiltrosProyectos.switchMap {
        repository.obtenerProyectosFiltradas(
            textoProyecto.value ?: "",
            inicioProyecto.value ?: OrdenarProyectosInicio.INICIO,
            finProyecto.value ?: OrdenarProyectoFin.FIN
        )
    }


    // Filtro etiquetas modificar
    private val listaEtiqueta = MutableLiveData<List<Etiqueta>>(emptyList<Etiqueta>())
    fun actualizarFiltroListaEtiquetaTareas(lista: List<Etiqueta>) {
        listaEtiqueta.value = lista
    }
    fun obtenerEtiquetasRestantes() : LiveData<List<Etiqueta>> = listaEtiqueta.switchMap {
            texto ->
        repository.obtenerEtiquetasRestantes(texto)
    }

    // Obtener tareas restantes
    private val listaTareas = MutableLiveData<List<Tarea>>(emptyList<Tarea>())
    fun actualizarFiltroListaTareaProyecto(lista: List<Tarea>) {
        listaTareas.value = lista
    }
    fun obtenerTareasRestantes() : LiveData<List<Tarea>> = listaTareas.switchMap {
            texto ->
        repository.obtenerTareasRestantes(texto)
    }

    // Generación tercera grafica de estadisticas
    fun obtenerDatosGrafico(timestampDiasSemana: LongArray): LiveData<List<BarEntry>> {
        // variable base (mediadiador, completa, no completas)
        val resultado = MediatorLiveData<List<BarEntry>>()
        val completas = timestampDiasSemana.map { it -> repository.tareasCompletadasPorDia(it)  }
        val noCompletas = timestampDiasSemana.map { it -> repository.tareasNoCompletadasPorDia(it)  }

        // Generación dataset
        val nuevoDataset = {
            // variable de datos
            val entradas = ArrayList<BarEntry>()
            // Bucle for metiendole los datos al resultado
            for (i in timestampDiasSemana.indices) {
                entradas.add(BarEntry(i.toFloat(), floatArrayOf(completas[i].value ?: 0f, noCompletas[i].value ?: 0f)))
            }
            // devuelve el resultado
            resultado.value = entradas
        }

        // Observers
        completas.forEach { resultado.addSource(it) { nuevoDataset() } }
        noCompletas.forEach { resultado.addSource(it) { nuevoDataset() } }

        // Devolvemos el dataset
        return resultado
    }




    // Metodos de inserción en la base de datos
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.insertarEtiqueta(etiqueta)
    suspend fun insertarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.insertarTareaConEtiqueta(tareaDTO)
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.insertarProyectoConTareaYEtiqueta(proyectoDTO)

    // Metodos de moficiación en la base de datos
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificarEtiqueta(etiqueta)
    suspend fun modificarTarea(tarea: Tarea) = repository.modificarTarea(tarea)
    suspend fun modificarTareaConEtiqueta(tareaDTO: TareaDTO) = repository.modificarTareaConEtiqueta(tareaDTO)
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.modificarProyectoConTareaYEtiqueta(proyectoDTO)




    // Metodos de eliminación en la base de datos
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = repository.eliminarEtiqueta(etiqueta)
    suspend fun eliminarTarea(tarea: Tarea) = repository.eliminarTarea(tarea)
    suspend fun eliminarProyectoConTareaYEtiqueta(proyectoVisualizado: ProyectoDTO) = repository.eliminarProyectoConTareaYEtiqueta(proyectoVisualizado)
    suspend fun limpiarTareasCompletas() = repository.limpiarTareasCompletas()

}