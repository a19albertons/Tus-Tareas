package com.example.tustareas.modelView

import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.repository.TusTareasRepository

class ModificarEtiquetasModel(private val repository: TusTareasRepository) {
    // Metodos de inserción en la base de datos
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = repository.modificacionEtiqueta.insertarEtiqueta(etiqueta)

    // Metodos de moficiación en la base de datos
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = repository.modificacionEtiqueta.modificarEtiqueta(etiqueta)
}