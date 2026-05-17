package com.example.tustareas.modelos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Clase que representa la relación N:N entre Proyecto y Etiquetas
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Entity(
    tableName = "ProyectoEtiquetas",
    primaryKeys = ["idProyecto", "idEtiqueta"],
    foreignKeys = [
        ForeignKey(
            entity = Etiqueta::class,
            parentColumns = ["id"],
            childColumns = ["idEtiqueta"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Proyecto::class,
            parentColumns = ["id"],
            childColumns = ["idProyecto"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["idProyecto"]),
        Index(value = ["idEtiqueta"]),
    ],
)
data class ProyectoEtiqueta(
    var idProyecto: Int,
    var idEtiqueta: Int,
)
