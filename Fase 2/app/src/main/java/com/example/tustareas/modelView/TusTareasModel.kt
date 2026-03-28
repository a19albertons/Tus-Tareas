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
    val modificarProyectosModel = ModificarProyectosModel(repository)
    val estadisticasModel = EstadisticasModel(repository)


    // Metodos de consulta de la base de datos

    fun obtenerEtiquetaPorID(id: Int) = repository.obtenerEtiquetaPorID(id)


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

    // Metodos de inserción en la base de datos
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.insertarEtiqueta(etiqueta)

    // Metodos de moficiación en la base de datos
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificarEtiqueta(etiqueta)




    // Metodos de eliminación en la base de datos
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = repository.eliminarEtiqueta(etiqueta)

    // Metodo de la actividad principal
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
}