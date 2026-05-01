package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import java.util.Date

/**
 * Clase creada especificamente para poseer todas las operaciones sobre bd de consultas en inicio fragment
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface InicioConsultas {
    /**
     * Obtiene todas las tareas de un día específico en tiempo con relación a su fecha límite.
     *
     * @param fecha La fecha del día para el cual obtener tareas.
     * @param estado El estado de las tareas a obtener, por defecto es Estado.EnTiempo. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<List<Tarea>> devuelve una lista de tareas que cumplen con los criterios.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from tareas where fechaLimite = :fecha AND estado = :estado")
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date, estado: Estado = Estado.EnTiempo): LiveData<List<Tarea>>

    /**
     * Obtiene todas las tareas retrasadas.
     *
     * @param estado El estado de las tareas a obtener, por defecto es Estado.Retrasada. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<List<Tarea>> devuelve una lista de tareas que cumplen con los criterios.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from tareas where estado = :estado")
    fun obtenerTareasRetrasadas(estado: Estado = Estado.Retrasada): LiveData<List<Tarea>>

    /**
     * Obtiene todas las tareas futuras no finalizadas.
     *
     * @param fecha La fecha para el cual obtener tareas.
     * @param estado El estado de las tareas a obtener, por defecto es Estado.EnTiempo. No pasar un parametro distinto dejar el valor por defecto.
     * @return LiveData<List<Tarea>> devuelve una lista de tareas que cumplen con los criterios.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("select * from tareas where (fechaLimite > :fecha OR fechaLimite is null) AND estado = :estado ORDER BY fechaLimite ASC")
    fun obtenerTareasProximas(fecha: Date, estado: Estado = Estado.EnTiempo): LiveData<List<Tarea>>
}