package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.dto.TareaDTO
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea
import java.util.Date

@Dao
interface TareaConsultas {


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

    // Obtencion tareas dto
    @Transaction
    @Query("select * from tareas where id = :id")
    fun obtenerTareaDTOPorID(id: Int): LiveData<TareaDTO>

    @Query("select * from tareas where id not in (:lista)")
    fun obtenerTareasRestantes(lista : List<Int>): LiveData<List<Tarea>>

    @Query("select count(*) from tareas where fechaLimite = :i and estado = :estado")
    fun tareasCompletadasPorDia(i: Long, estado: Estado = Estado.Completada): LiveData<Float>

    @Query("select count(*) from tareas where fechaLimite = :i and estado != :estado")
    fun tareasNoCompletadasPorDia(i: Long, estado: Estado = Estado.Completada): LiveData<Float>

    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasCompletas(estado: Estado = Estado.Completada): LiveData<Int>
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasPendientes(estado: Estado = Estado.EnTiempo): LiveData<Int>
    @Query("select count(*) from tareas where estado = :estado")
    fun obtenerCantidadTareasRetrasadas(estado: Estado = Estado.Retrasada): LiveData<Int>

    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio: Long, fechaFin: Long, estado: Estado = Estado.Completada): LiveData<Long>

    @Query("select count(*) from tareas where fechaLimite between :fechaInicio and :fechaFin and estado = :estado")
    fun obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio: Long, fechaFin: Long, estado: Estado = Estado.EnTiempo): LiveData<Long>


}