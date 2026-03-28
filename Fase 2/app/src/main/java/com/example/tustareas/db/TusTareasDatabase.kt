package com.example.tustareas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tustareas.dao.EtiquetaConsultas
import com.example.tustareas.dao.EtiquetaModificaciones
import com.example.tustareas.dao.InicioConsultas
import com.example.tustareas.dao.ProyectoConsultas
import com.example.tustareas.dao.ProyectoModificaciones
import com.example.tustareas.dao.TareaConsultas
import com.example.tustareas.dao.TareaModificaciones
import com.example.tustareas.modelos.Convertidor
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.ProyectoEtiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta

/**
 * Base de datos de tus tareas
 */
@Database(entities = [Proyecto::class, Tarea::class, Etiqueta::class, ProyectoEtiqueta::class, TareaEtiqueta::class], version = 1, exportSchema = false)
@TypeConverters(Convertidor::class)
abstract class TusTareasDatabase : RoomDatabase() {

    abstract fun tareaConsultas(): TareaConsultas
    abstract fun etiquetaConsultas(): EtiquetaConsultas
    abstract fun proyectoConsultas(): ProyectoConsultas

    abstract fun etiquetaModificaciones(): EtiquetaModificaciones
    abstract fun tareaModificaciones(): TareaModificaciones
    abstract fun proyectoModificaciones(): ProyectoModificaciones

    abstract fun inicioConsultas(): InicioConsultas

    companion object {
        @Volatile
        private var INSTANCE: TusTareasDatabase? = null

        fun getDatabase(context: Context): TusTareasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder<TusTareasDatabase>(
                    context.applicationContext,
                    TusTareasDatabase::class.java,
                    "baseDatos.db"
                ).createFromAsset("baseDatos.db").build()
                INSTANCE = instance
                instance
            }
        }

    }
}