package com.example.tustareas.dto

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.ProyectoEtiqueta
import com.example.tustareas.modelos.Tarea
import kotlinx.parcelize.Parcelize

/**
 * Clase que representa el Data Transfer Object entre Proyecto y la lista de tareas, además de Proyecto con sus etiquetas
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Parcelize
data class ProyectoDTO (
    // Integra un proyecto
    @Embedded var proyecto: Proyecto,
    // Obtiene una lista de etiquetas relacionada con el proyecto a través de la tabla de relación ProyectoEtiqueta
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(ProyectoEtiqueta::class, parentColumn = "idProyecto", entityColumn = "idEtiqueta")
    )
    var etiquetas: List<Etiqueta>,
    // Obtiene una lista de tareas relacionada con el proyecto a través del campo idProyecto en la tabla de tareas
    @Relation(
        parentColumn = "id",
        entityColumn = "idProyecto"
    )
    var tareas: List<Tarea>
) : Parcelable {
}