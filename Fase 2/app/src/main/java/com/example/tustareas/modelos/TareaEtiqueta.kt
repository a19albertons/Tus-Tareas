package com.example.tustareas.modelos

import androidx.room.Entity

/**
 * Clase que representa la relación N:N entre Tarea y Etiquetas
 */
@Entity(tableName = "TareaEtiquetas", primaryKeys = ["idTarea", "idEtiqueta"])
data class TareaEtiqueta(
    var idTarea: Int,
    var idEtiqueta: Int
) {
}