package com.example.tustareas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tustareas.dao.ActivityMainConsultas
import com.example.tustareas.dao.CrearEtiquetaConsultas
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

/**
 * Base de datos de tus tareas.
 * Ramificación especifica de TusTareasDatabase paara el entorno de pre-producción. La principal diferencia es cargar una base de datos de pruebas y sin sqlCypher para evitar un problema de compatiblidad por el cifrado que no tiene.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Database(
    entities = [Proyecto::class, Tarea::class, Etiqueta::class, ProyectoEtiqueta::class, TareaEtiqueta::class, Notificacion::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Convertidor::class)
abstract class TusTareasDatabase : RoomDatabase() {
    /**
     * Obtiene las consultas de inicio
     *
     * @return InicioConsultas devuelve las consultas de inicio.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun inicioConsultas(): InicioConsultas

    /**
     * Obtiene las consultas de ver mas
     *
     * @return VerMasConsulta devuelve las consultas de ver mas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun verMasConsultas(): VerMasConsulta

    /**
     * Obtiene las consultas de listar tareas
     *
     * @return ListarTareasConsultas devuelve las consultas de listar tareas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun listarTareasConsultas(): ListarTareasConsultas

    /**
     * Obtiene las consultas de tarea detalles
     *
     * @return TareaDetallesConsulta devuelve las consultas de tarea detalles.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun tareaDetallesConsulta(): TareaDetallesConsulta

    /**
     * Obtiene las consultas de modificar tarea
     *
     * @return ModificarTareaConsultas devuelve las consultas de modificar tarea.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun modificarTareaConsultas(): ModificarTareaConsultas

    /**
     * Obtiene las consultas de listar proyectos
     *
     * @return ListarProyectosConsultas devuelve las consultas de listar proyectos.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun listarProyectosConsultas(): ListarProyectosConsultas

    /**
     * Obtiene las consultas de proyecto detalles
     *
     * @return ProyectoDetallesConsultas devuelve las consultas de proyecto detalles.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun proyectoDetallesConsultas(): ProyectoDetallesConsultas

    /**
     * Obtiene las consultas de modificar proyecto
     *
     * @return ModificarProyectoConsultas devuelve las consultas de modificar proyecto.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun modificarProyectoConsultas(): ModificarProyectoConsultas

    /**
     * Obtiene las consultas de estadisticas
     *
     * @return EstadisticasConsultas devuelve las consultas de estadisticas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun estadisticasConsultas(): EstadisticasConsultas

    /**
     * Obtiene las consultas de activity main
     *
     * @return ActivityMainConsultas devuelve las consultas de activity main.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun activityMainConsultas(): ActivityMainConsultas

    /**
     * Obtiene las consultas de listar etiquetas
     *
     * @return ListarEtiquetasConsultas devuelve las consultas de listar etiquetas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun listarEtiquetasConsultas(): ListarEtiquetasConsultas

    /**
     * Obtiene las consultas de etiqueta detalles
     *
     * @return EtiquetaDetallesConsultas devuelve las consultas de etiqueta detalles.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun etiquetaDetallesConsultas(): EtiquetaDetallesConsultas

    /**
     * Obtiene las consultas de modificar etiqueta
     *
     * @return ModificarEtiquetaConsultas devuelve las consultas de modificar etiqueta.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun modificarEtiquetaConsultas(): ModificarEtiquetaConsultas

    /**
     * Obtiene las consultas de worker
     *
     * @return WorkerConsultas devuelve las consultas de worker.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    abstract fun workerConsultas(): WorkerConsultas

    abstract fun crearEtiquetaConsultas(): CrearEtiquetaConsultas

    companion object {
        @Volatile
        private var instanciaDB: TusTareasDatabase? = null

        /**
         * Obtiene la instancia de la base de datos. Si no existe, la crea.
         *
         * @param context El contexto de la aplicación.
         * @return TusTareasDatabase devuelve la instancia de la base de datos.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun getDatabase(context: Context): TusTareasDatabase =
            instanciaDB ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            TusTareasDatabase::class.java,
                            "baseDatos.db",
                        )
                        // Crea la base de datos desde el asset
                        .createFromAsset("baseDatos.db")
                        // Habilita el modo WAL para mejorar las consultas concurrentes por la carga adicional del cifrado
                        .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                        // Capa de optimizaciones para mejorar el rendimiento de la base de datos.
                        .addCallback(
                            object : Callback() {
                                override fun onOpen(db: SupportSQLiteDatabase) {
                                    super.onOpen(db)
                                    db.execSQL("PRAGMA cache_size = -4000") // 4MB de caché
                                    db.execSQL("PRAGMA temp_store = MEMORY") // Usa la ram como alamcenamiento temporal
                                    db.execSQL("PRAGMA synchronous = NORMAL") // Se recomienda con WAL
                                }
                            },
                        )
                        // La construye
                        .build()

                // Prepara la conexión
                instance.openHelper.writableDatabase

                // genera la instancia singleston
                instanciaDB = instance

                // valor a devolver que requiere synchronized
                instance
            }
    }
}
