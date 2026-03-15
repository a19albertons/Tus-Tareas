package com.example.tustareas.modelos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Clase que representa la tabla etiquetas en la bd
 */
@Parcelize
@Entity(tableName = "etiquetas")
data class Etiqueta(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var nombre: String,
    var descripcion: String? = null
) : Parcelable