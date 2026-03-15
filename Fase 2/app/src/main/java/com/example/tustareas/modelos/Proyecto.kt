package com.example.tustareas.modelos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import kotlinx.parcelize.Parcelize

/**
 * Clase que representa la tabla proyectos en la bd
 */
@Parcelize
@Entity(tableName = "proyectos")
data class Proyecto (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nombre: String,
    var descripcion: String? = null,
    var fechaCreacion: Date,
    var fechaInicio: Date? = null,
    var fechaFin: Date ? = null
    // La lista de  tareas se declara en la hija como Foreign key
    // La lista de etiquetas tiene el mismo problema que en Tareas (N:N)
) : Parcelable {
}