package com.example.tustareas.modelos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

/**
 * Clase que representa la tabla tareas en la bd
 */
@Entity(
    tableName = "tareas",
    foreignKeys = [
        ForeignKey(
            entity = Proyecto::class,
            parentColumns = ["id"], // PK de proyecto (padre)
            childColumns = ["idProyecto"], // FK en tareas (hija)
        )
    ]
)
data class Tarea (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nombre: String,
    var descripcion: String,
    var fechaLimite: Date, // Hay que construirlo a partir del timestamp
    var prioridad: Prioridad, // Hay que hacer una clase para convertir este enum
    var fechaCreacion: Date, // Hay que construirlo a partir del timestamp
    var estado: Estado, // Hay que hacer una clase para convertir este enum
    // La lista de etiquetas va en una clase distinta room se lleva mal con las N:N
    var idProyecto: Int // Clave foránea a proyecto
)