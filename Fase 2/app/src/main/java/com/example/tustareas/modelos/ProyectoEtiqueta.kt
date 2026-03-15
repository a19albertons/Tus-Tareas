package com.example.tustareas.modelos

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Clase que representa la relación N:N entre Proyecto y Etiquetas
 */
@Entity(
    tableName = "ProyectoEtiquetas",
    primaryKeys = ["idProyecto", "idEtiqueta"],
    foreignKeys = [
        ForeignKey(
            entity = Etiqueta::class,
            parentColumns = ["id"],
            childColumns = ["idEtiqueta"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Proyecto::class,
            parentColumns = ["id"],
            childColumns = ["idProyecto"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProyectoEtiqueta(
    var idProyecto: Int,
    var idEtiqueta: Int
) {
}