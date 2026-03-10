package com.example.tustareas.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.ProyectoEtiqueta

/**
 * Clase que representa el Data Transfer Object entre Proyecto y la lista de tareas
 */
data class ProyectoDTO (
    @Embedded var proyecto: Proyecto,
    @Relation(
        parentColumn = "idProyecto",
        entityColumn = "idEtiqueta",
        associateBy = Junction(ProyectoEtiqueta::class)
    )
    var etiquetas: List<Etiqueta>

) {
}