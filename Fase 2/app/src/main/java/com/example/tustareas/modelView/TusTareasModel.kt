package com.example.tustareas.modelView

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
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
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.repository.TusTareasRepository
import com.github.mikephil.charting.data.BarEntry
import kotlin.apply
import androidx.core.content.edit
import com.example.tustareas.util.LanguageHelper

/**
 * ViewModel que une la aplicacion con la base de datos
 */
class TusTareasModel(application: Application): AndroidViewModel(application) {
    // Invocacion repositorio
    private val repository = TusTareasRepository(TusTareasDatabase.getDatabase( application))
    val inicio = InicioModel(repository)
    val verMas = VerMasModel(repository)
    val listarTareas = ListarTareasModel(repository)
    val tareaDetalles = TareaDetallesModel(repository)
    val modificarTareasModel = ModificarTareasModel(repository)
    val listarProyectosModel = ListarProyectosModel(repository)
    val proyectoDetallesModel = ProyectoDetallesModel(repository)



    // Metodos de consulta de la base de datos

    fun obtenerEtiquetaPorID(id: Int) = repository.obtenerEtiquetaPorID(id)
    fun obtenerTodasLasTareas() = repository.obtenerTodasLasTareas()
    fun obtenerCantidadTareasCompletas() = repository.obtenerCantidadTareasCompletas()
    fun obtenerCantidadTareasPendientes() = repository.obtenerCantidadTareasPendientes()
    fun obtenerCantidadTareasRetrasadas() = repository.obtenerCantidadTareasRetrasadas()






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
    // Generacion rueda (primer grafico)
    fun obtenerRueda(fechaInicio: Long, fechaFin: Long): LiveData<Pair<Long, Long>> {
        // variable base
        val resultado = MediatorLiveData<Pair<Long, Long>>()
        val completas = repository.obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio, fechaFin)
        val noCompletas = repository.obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio, fechaFin)

        // actualizador
        val valores = {
            val c = completas.value ?: 0
            val nc = noCompletas.value ?: 0
            resultado.value = Pair(c, nc)
        }

        // Observar
        resultado.addSource(completas) { valores() }
        resultado.addSource(noCompletas) { valores() }

        // resultado
        return resultado


    }





    // Metodos de inserción en la base de datos
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.insertarEtiqueta(etiqueta)
    suspend fun insertarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.insertarProyectoConTareaYEtiqueta(proyectoDTO)

    // Metodos de moficiación en la base de datos
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificarEtiqueta(etiqueta)
    suspend fun modificarTarea(tarea: Tarea) = repository.modificarTarea(tarea)
    suspend fun modificarProyectoConTareaYEtiqueta(proyectoDTO: ProyectoDTO) = repository.modificarProyectoConTareaYEtiqueta(proyectoDTO)




    // Metodos de eliminación en la base de datos
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = repository.eliminarEtiqueta(etiqueta)
    suspend fun limpiarTareasCompletas() = repository.limpiarTareasCompletas()


    // Ajustes
    private val prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val idioma = MutableLiveData<String>().apply {
        value = prefs.getString("idioma", "sistema")
    }

    fun setIdioma(idioma: String) {
        prefs.edit { putString("idioma", idioma) }
        this.idioma.value = idioma
        LanguageHelper.aplicarIdioma(LanguageHelper.etiquetaIdioma(idioma))
    }

    val tema = MutableLiveData<Int>().apply {
        // Valor del sistema (movil)
        value = prefs.getInt("tema", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun setTema (tema: Int) {
        prefs.edit { putInt("tema", tema) }
        this.tema.value = tema
        AppCompatDelegate.setDefaultNightMode(tema)
    }





    // Legacy
    private val listaEtiqueta = MutableLiveData<List<Etiqueta>>(emptyList<Etiqueta>())
    fun actualizarFiltroListaEtiquetaTareas(lista: List<Etiqueta>) {
        listaEtiqueta.value = lista
    }
    fun obtenerEtiquetasRestantes() : LiveData<List<Etiqueta>> = listaEtiqueta.switchMap {
            texto ->
        repository.obtenerEtiquetasRestantes(texto)
    }
}