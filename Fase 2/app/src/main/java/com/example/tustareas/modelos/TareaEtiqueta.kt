package com.example.tustareas.modelos

import androidx.room.Entity

@Entity(tableName = "TareaEtiquetas", primaryKeys = ["idTarea", "idEtiqueta"])
data class TareaEtiqueta(
    var idTarea: Int,
    var idEtiqueta: Int
) {
}