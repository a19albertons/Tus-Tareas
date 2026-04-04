package com.example.tustareas.modelos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Clase que representa la relación N:N entre Tarea y Etiquetas
 */
@Entity(
    tableName = "TareaEtiquetas",
    primaryKeys = ["idTarea", "idEtiqueta"],
    foreignKeys = [
        ForeignKey(
            entity = Etiqueta::class,
            parentColumns = ["id"],
            childColumns = ["idEtiqueta"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tarea::class,
            parentColumns = ["id"],
            childColumns = ["idTarea"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idTarea"]),
        Index(value = ["idEtiqueta"])
    ]
)
data class TareaEtiqueta(
    var idTarea: Int,
    var idEtiqueta: Int
) {
}