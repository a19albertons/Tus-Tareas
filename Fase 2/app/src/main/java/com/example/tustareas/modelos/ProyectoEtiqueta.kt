package com.example.tustareas.modelos

import androidx.room.Entity

/**
 * Clase que representa la relación N:N entre Proyecto y Etiquetas
 */
@Entity(tableName = "ProyectoEtiquetas", primaryKeys = ["idProyecto", "idEtiqueta"])
data class ProyectoEtiqueta(
    var idProyecto: Int,
    var idEtiqueta: Int
) {
}