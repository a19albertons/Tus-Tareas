package com.example.tustareas.modelos

import androidx.room.Entity

@Entity(tableName = "ProyectoEtiquetas", primaryKeys = ["idProyecto", "idEtiqueta"])
data class ProyectoEtiqueta(
    var idProyecto: Int,
    var idEtiqueta: Int
) {
}