package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Notificacion
import com.example.tustareas.modelos.Tarea
import java.util.Calendar
import java.util.Date

/**
 * Clase que representa las consultas contra la bd de las tareas programadas
 */
@Dao
interface WorkerConsultas {
    // Actualiza todas las tareas retradas de en tiempo a retrasadas cuando corresponde
    @Query("UPDATE tareas SET estado = :estadoNuevo where fechaLimite < :fecha AND estado = :estado")
    fun actualizarEstado(fecha: Date = Date(), estado: Estado = Estado.EnTiempo, estadoNuevo: Estado = Estado.Retrasada)



    // Obtener tareas con fecha limite retrasada y no estan completas para la alarma
    // Asumimos la posibilidad hipotetica de que la tarea programadas se ejecute despues de esto
    @Query("SELECT * FROM tareas WHERE fechaLimite < :date AND estado != :estado")
    suspend fun tareasRetrasadasAlarma(date: Date = Date().apply {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }, estado: Estado = Estado.Completada) : List<Tarea>

    // Obtener notificaciones
    @Query("SELECT * FROM notificaciones")
    fun obtenerTodasLasNotificaciones() : List<Notificacion>

    // Añade una notificación a la bd
    @Insert
    fun anadirNotificacion(notificacion: Notificacion)

    // Se usa 0 en lugar de false porque hay un problema con el soporte api previo a 30 en materia de compatibilidad
    @Query("Select * from notificaciones where leido = 0")
    suspend fun enviarNotificaciones() : List<Notificacion>

}