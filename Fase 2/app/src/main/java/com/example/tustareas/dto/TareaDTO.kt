package com.example.tustareas.dto

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta
import kotlinx.parcelize.Parcelize

/**
 * Clase que representa el Data Transfer Object entre Tarea y la lista de etiquetas
 */
@Parcelize
data class TareaDTO (
    @Embedded var tarea: Tarea,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(TareaEtiqueta::class, parentColumn = "idTarea", entityColumn = "idEtiqueta")
    )
    var etiquetas: List<Etiqueta>
) : Parcelable {

}