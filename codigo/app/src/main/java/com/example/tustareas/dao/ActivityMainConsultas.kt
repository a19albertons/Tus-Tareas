package com.example.tustareas.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.tustareas.modelos.Estado

/**
 * Clase que representa las consultas de la actividad principal
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ActivityMainConsultas {
    /**
     * Elimina las tareas completadas de la base de datos.
     *
     * @param estado El estado de las tareas a eliminar, por defecto es Estado.Completada. No pasar un parametro distinto dejar el valor por defecto
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("DELETE FROM tareas WHERE estado = :estado")
    suspend fun limpiarTareasCompletas(estado: Estado = Estado.COMPLETADA)

    /**
     * Marca una notificación como leída en la base de datos.
     *
     * @param idNotificacion El ID de la notificación a marcar como leída.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("UPDATE notificaciones SET leido = 1 where id = :idNotificacion")
    suspend fun marcarNotificacionComoLeida(idNotificacion: Int)
}