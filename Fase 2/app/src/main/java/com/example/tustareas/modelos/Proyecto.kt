package com.example.tustareas.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "proyectos")
data class Proyecto (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nombre: String,
    var descripcion: String,
    var fechaCreacion: Date,
    var fechaInicio: Date,
    var fechaFin: Date
    // La lista de  tareas se declara en la hija como Foreign key
    // La lista de etiquetas tiene el mismo problema que en Tareas (N:N)
) {
}