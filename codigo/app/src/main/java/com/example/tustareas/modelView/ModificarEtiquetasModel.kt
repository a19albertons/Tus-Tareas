package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.ModificarEtiquetasRepository
import com.example.tustareas.repository.TusTareasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Clase que representa al submodelo de modificar etiqueta.
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class ModificarEtiquetasModel @Inject constructor(
    application: Application,
    private val repository: ModificarEtiquetasRepository
) : AndroidViewModel(application) {

    /**
     * Inserta una nueva etiqueta en la base de datos
     *
     * @param etiqueta La etiqueta a insertar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.insertarEtiqueta(etiqueta)

    /**
     * Modifica una etiqueta en la base de datos
     *
     * @param etiqueta La etiqueta a modificar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificarEtiqueta(etiqueta)
}