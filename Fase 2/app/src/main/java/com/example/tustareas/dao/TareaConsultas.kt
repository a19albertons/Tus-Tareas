package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import java.util.Date

@Dao
interface TareaConsultas {
    @Transaction
    @Query("select * from tareas where fechaLimite = :fecha AND estado = 'EnTiempo'")
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date): LiveData<List<Tarea>>

    @Transaction
    @Query("select * from tareas where fechaLimite < :fecha AND (estado = 'EnTiempo' OR estado = 'Retrasada')")
    fun obtenerTareasRetrasadas(fecha: Date): LiveData<List<Tarea>>

    @Transaction
    @Query("select * from tareas where (fechaLimite > :fecha OR fechaLimite = null) AND estado = 'EnTiempo' ORDER BY fechaLimite ASC")
    fun obtenerTareasProximas(fecha: Date): LiveData<List<Tarea>>

    @Transaction
    @Query("select * from tareas")
    fun obtenerTodasLasTareas(): LiveData<List<Tarea>>

    // Creamos una constante con la consulta previamente usada
    companion object {
        const val BASE_FILTRADO_TAREAS = "select * from tareas " + // pedir datos
                "where prioridad IN (:prioridad) " + // filtro prioridad
                "AND estado IN (:estado) " + // filtro estado
                "AND (LOWER(nombre) like LOWER('%' || :textoTarea || '%') " + // filtro nombre tarea
                "OR LOWER(descripcion) like LOWER('%' || :textoTarea || '%') " + // filtro descripción tarea
                "OR id IN (select idTarea from TareaEtiquetas " + // obtener datos de la subconsulta para el nombre de la etiqueta
                "join etiquetas on TareaEtiquetas.idEtiqueta = etiquetas.id " + // Comprobación de ids
                "where LOWER(etiquetas.nombre) LIKE LOWER('%' || :textoTarea || '%'))) "

    }

    // Ordenacion fecha creación ascendente
    @Transaction
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaLimite ASC")
    fun obtenerTareasFiltradasFechaLimiteAsc(prioridad: Array<Prioridad>, estado: Array<Estado>, textoTarea: String): LiveData<List<Tarea>>

    // Ordenacion fecha creación descendente
    @Transaction
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaLimite DESC")
    fun obtenerTareasFiltradasFechaLimiteDes(prioridad: Array<Prioridad>, estado: Array<Estado>, textoTarea: String): LiveData<List<Tarea>>

    // Ordenacion fecha limite ascendente
    @Transaction
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaCreacion ASC")
    fun obtenerTareasFiltradasFechaCreacionAsc(prioridad: Array<Prioridad>, estado: Array<Estado>, textoTarea: String): LiveData<List<Tarea>>

    // Ordenacion fecha limite descendente
    @Transaction
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaCreacion DESC")
    fun obtenerTareasFiltradasFechaCreacionDes(prioridad: Array<Prioridad>, estado: Array<Estado>, textoTarea: String): LiveData<List<Tarea>>


}