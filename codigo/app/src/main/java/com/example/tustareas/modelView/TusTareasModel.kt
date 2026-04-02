package com.example.tustareas.modelView

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.repository.TusTareasRepository
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
    val modificarTareas = ModificarTareasModel(repository)
    val listarProyectos = ListarProyectosModel(repository)
    val proyectoDetalles = ProyectoDetallesModel(repository)
    val modificarProyectos = ModificarProyectosModel(repository)
    val estadisticas = EstadisticasModel(repository)
    val listarEtiquetas = ListarEtiquetasModel(repository)
    val etiquetaDetalles = EtiquetaDetallesModel(repository)
    val modificarEtiquetas = ModificarEtiquetasModel(repository)



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