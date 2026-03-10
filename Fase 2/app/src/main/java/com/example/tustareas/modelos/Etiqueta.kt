package com.example.tustareas.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "etiquetas")
data class Etiqueta(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var nombre: String,
    var descripcion: String
) {
}