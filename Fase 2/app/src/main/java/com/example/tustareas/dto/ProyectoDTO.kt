package com.example.tustareas.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.ProyectoEtiqueta
import com.example.tustareas.modelos.Tarea

/**
 * Clase que representa el Data Transfer Object entre Proyecto y la lista de tareas, además de Proyecto con sus etiquetas
 */
data class ProyectoDTO (
    @Embedded var proyecto: Proyecto,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(ProyectoEtiqueta::class, parentColumn = "idProyecto", entityColumn = "idEtiqueta")
    )
    var etiquetas: List<Etiqueta>,
    @Relation(
        parentColumn = "id",
        entityColumn = "idProyecto"
    )
    var tareas: List<Tarea>
) {
}