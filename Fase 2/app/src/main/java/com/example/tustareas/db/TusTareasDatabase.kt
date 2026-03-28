package com.example.tustareas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tustareas.dao.ActivityMainConsultas
import com.example.tustareas.dao.EstadisticasConsultas
import com.example.tustareas.dao.EtiquetaDetallesConsultas
import com.example.tustareas.dao.InicioConsultas
import com.example.tustareas.dao.ListarEtiquetasConsultas
import com.example.tustareas.dao.ListarProyectosConsultas
import com.example.tustareas.dao.ListarTareasConsultas
import com.example.tustareas.dao.ModificarEtiquetaConsultas
import com.example.tustareas.dao.ModificarProyectoConsultas
import com.example.tustareas.dao.ModificarTareaConsultas
import com.example.tustareas.dao.ProyectoDetallesConsultas
import com.example.tustareas.dao.TareaDetallesConsulta
import com.example.tustareas.dao.VerMasConsulta
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


    // Consultas de inicio
    abstract fun inicioConsultas(): InicioConsultas
    // Consultas de ver mas
    abstract fun verMasConsultas(): VerMasConsulta
    // Consultas de listar tareas
    abstract fun listarTareasConsultas(): ListarTareasConsultas
    // Consultas de tarea detalles
    abstract fun tareaDetallesConsulta(): TareaDetallesConsulta
    // consultas modificar tarea
    abstract fun modificarTareaConsultas(): ModificarTareaConsultas
    // Consultas listar proyectos
    abstract fun listarProyectosConsultas(): ListarProyectosConsultas
    abstract fun proyectoDetallesConsultas(): ProyectoDetallesConsultas
    abstract fun modificarProyectoConsultas(): ModificarProyectoConsultas
    abstract fun estadisticasConsultas(): EstadisticasConsultas
    abstract fun activityMainConsultas(): ActivityMainConsultas
    abstract fun listarEtiquetasConsultas(): ListarEtiquetasConsultas
    abstract fun etiquetaDetallesConsultas(): EtiquetaDetallesConsultas
    abstract fun modificarEtiquetaConsultas(): ModificarEtiquetaConsultas

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