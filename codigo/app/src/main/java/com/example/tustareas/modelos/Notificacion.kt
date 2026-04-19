package com.example.tustareas.modelos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Clase que getiona la tabla de notificaciones en la bd
 */
@Entity(
    tableName = "notificaciones",
    foreignKeys = [
        ForeignKey(
            entity = Tarea::class,
            parentColumns = ["id"], // PK de tarea (padre)
            childColumns = ["idTarea"], // Fk en notifiacaciones (hija)
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idTarea"])
    ]
)
@Parcelize
data class Notificacion(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var titulo : String,
    var mensaje : String,
    var leido: Boolean,
    var idTarea: Int
) : Parcelable{

}