package com.example.tustareas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.example.tustareas.dao.WorkerConsultas
import com.example.tustareas.modelos.Convertidor
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Notificacion
import com.example.tustareas.modelos.Proyecto
import com.example.tustareas.modelos.ProyectoEtiqueta
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.modelos.TareaEtiqueta
import com.example.tustareas.security.SqlCipherKeyManager

/**
 * Base de datos de tus tareas
 */
@Database(entities = [Proyecto::class, Tarea::class, Etiqueta::class, ProyectoEtiqueta::class, TareaEtiqueta::class, Notificacion::class], version = 1, exportSchema = false)
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
    // Consultas proyecto detalles
    abstract fun proyectoDetallesConsultas(): ProyectoDetallesConsultas
    // Consultas modificar proyecto
    abstract fun modificarProyectoConsultas(): ModificarProyectoConsultas
    // Consultas de estadisticas
    abstract fun estadisticasConsultas(): EstadisticasConsultas
    // Consultas de activity main
    abstract fun activityMainConsultas(): ActivityMainConsultas
    // Consultas de listar etiquetas
    abstract fun listarEtiquetasConsultas(): ListarEtiquetasConsultas
    // Consultas de etiqueta detalles
    abstract fun etiquetaDetallesConsultas(): EtiquetaDetallesConsultas
    // Consultas de modificar etiqueta
    abstract fun modificarEtiquetaConsultas(): ModificarEtiquetaConsultas
    // Consultas de worker
    abstract fun workerConsultas(): WorkerConsultas

    companion object {
        @Volatile
        private var INSTANCE: TusTareasDatabase? = null

        fun getDatabase(context: Context): TusTareasDatabase {
            return INSTANCE ?: synchronized(this) {
                when (context.packageName) {
                    "com.example.tustareas" -> {
                        // Carga la libreria de sqlcipher
                        System.loadLibrary("sqlcipher")
                        // Obtiene las preferencias
                        val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                        // Instancia al gesto del cifrado
                        val sqlCipherKeyManager = SqlCipherKeyManager(sharedPreferences)
                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            TusTareasDatabase::class.java,
                            "baseDatos.db"
                        )
                            // Impelmenta el cifrado
                            .openHelperFactory(sqlCipherKeyManager.getSupportFactory())
                            // La construye
                            .build()
                        INSTANCE = instance
                        instance
                    }
                    else -> {
                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            TusTareasDatabase::class.java,
                            "baseDatos.db"
                        )
                            // Crea la base de datos desde el asset
                            .createFromAsset("baseDatos.db")
                            // La construye
                            .build()
                        INSTANCE = instance
                        instance
                    }
                }

            }
        }

    }
}