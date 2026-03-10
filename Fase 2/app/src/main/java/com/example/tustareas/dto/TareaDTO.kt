package com.example.tustareas.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta

/**
 * Clase que representa el Data Transfer Object entre Tarea y la lista de etiquetas
 */
class TareaDTO (
    @Embedded var tarea: Tarea,
    @Relation(
        parentColumn = "idTarea",
        entityColumn = "idEtiqueta",
        associateBy = Junction(TareaEtiqueta::class)

    )
    var etiquetas: List<Etiqueta>
) {

}