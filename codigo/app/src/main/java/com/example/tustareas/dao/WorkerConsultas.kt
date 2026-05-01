package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Notificacion
import com.example.tustareas.modelos.Tarea
import com.example.tustareas.util.DateHelper
import java.util.Date

/**
 * Clase que representa las consultas contra la bd de las tareas programadas
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface WorkerConsultas {

    /**
     * Actualiza el estado de las tareas que tienen fecha límite anterior a la fecha actual y que están en tiempo, cambiándolas a retrasadas.
     *
     * @param fecha La fecha actual a las 00:00 UTC, por defecto se utiliza la función DateHelper.fechaMediaNocheUTC() para obtenerla. No pasar un parametro distinto dejar el valor por defecto.
     * @param estado El estado actual de las tareas a actualizar, por defecto es Estado.EnTiempo. No pasar un parametro distinto dejar el valor por defecto.
     * @param estadoNuevo El nuevo estado que se asignará a las tareas que cumplan con los criterios, por defecto es Estado.Retrasada. No pasar un parametro distinto dejar el valor por defecto.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("UPDATE tareas SET estado = :estadoNuevo where fechaLimite < :fecha AND estado = :estado")
    fun actualizarEstado(fecha: Date = DateHelper.fechaMediaNocheUTC(), estado: Estado = Estado.EN_TIEMPO, estadoNuevo: Estado = Estado.RETRASADA)


    /**
     * Obtiene una lista de tareas que estan retrasadas y no tienen el estado completada.
     *
     * @param date La fecha actual a las 00:00 UTC, por defecto se utiliza la función DateHelper.fechaMediaNocheUTC() para obtenerla. No pasar un parametro distinto dejar el valor por defecto.
     * @param estado El estado de las tareas a filtrar, por defecto es Estado.Completada. No pasar un parametro distinto dejar el valor por defecto.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("SELECT * FROM tareas WHERE fechaLimite < :date AND estado != :estado")
    suspend fun tareasRetrasadasAlarma(date: Date = DateHelper.fechaMediaNocheUTC(), estado: Estado = Estado.COMPLETADA) : List<Tarea>


    /**
     * Obtiene las notificaciones actuales de la base de datos.
     *
     * @return List<Notificacion> con las notificaciones actuales de la base de datos.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("SELECT * FROM notificaciones")
    fun obtenerTodasLasNotificaciones() : List<Notificacion>

    /**
     * Añade una notificación a la base de datos.
     *
     * @param notificacion La notificación a añadir a la base de datos.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Insert
    fun anadirNotificacion(notificacion: Notificacion)

    /**
     * Obtiene una lista de notificaciones que no han sido leídas.
     *
     * @return List<Notificacion> con las notificaciones que no han sido leídas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("Select * from notificaciones where leido = 0")
    suspend fun enviarNotificaciones() : List<Notificacion>

}