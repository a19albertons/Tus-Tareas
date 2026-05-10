package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.EtiquetaDetallesRepository
import com.example.tustareas.repository.TusTareasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Clase que representa al submodelo de etiqueta detalles
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class EtiquetaDetallesModel @Inject constructor(
    application: Application,
    private val repository: EtiquetaDetallesRepository
) : AndroidViewModel(application) {
    /**
     * Obtiene una etiqueta por su id
     *
     * @param id El ID de la etiqueta a obtener
     * @return La etiqueta correspondiente. Debería existir
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerEtiquetaPorID(id: Int) = repository.obtenerEtiquetaPorID(id)

    /**
     * Elimina una etiqueta de la base de datos
     *
     * @param etiqueta La etiqueta a eliminar
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) = repository.eliminarEtiqueta(etiqueta)
}