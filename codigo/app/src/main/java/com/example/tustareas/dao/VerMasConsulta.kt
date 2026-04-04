package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Tarea
import java.util.Date

/**
 * Clase creada especificamente para poseer todas las operaciones sobre bd de consultas en ver mas fragment
 */
@Dao
interface VerMasConsulta {
    // Obtiene toda las tareas de un dia con el el filtro correspondiente
    @Transaction
    @Query("select * from tareas " + // Obtener todo
            "where fechaLimite = :fecha " + // Fecha limite
            "AND estado = :estado " + // El estado
            "AND (LOWER(nombre) like LOWER('%' || :texto || '%') " + // El nombre
            "OR LOWER(descripcion) like LOWER('%' || :texto || '%') " + // la descripcion
            "OR id IN (select idTarea from TareaEtiquetas " + // obtener datos de la subconsulta para el nombre de la etiqueta
            "join etiquetas on TareaEtiquetas.idEtiqueta = etiquetas.id " + // Comprobación de ids en la relacion many to many
            "where LOWER(etiquetas.nombre) LIKE LOWER('%' || :texto || '%'))) ") // El nombre de la etiqueta
    fun obtenerTareasTerminanDiaEspecificoConFiltro(texto: String, fecha: Date = Date(), estado: Estado = Estado.EnTiempo): LiveData<List<Tarea>>



    // Obtiene toda las tareas retrasadas con el el filtro correspondiente
    @Transaction
    @Query("select * from tareas " + // obtener todo
            "where estado = :estado " + // filtro estado
            "AND (LOWER(nombre) like LOWER('%' || :texto || '%') " + // El nombre
            "OR LOWER(descripcion) like LOWER('%' || :texto || '%') " + // la descripcion
            "OR id IN (select idTarea from TareaEtiquetas " + // obtener datos de la subconsulta para el nombre de la etiqueta
            "join etiquetas on TareaEtiquetas.idEtiqueta = etiquetas.id " + // Comprobación de ids en la relacion many to many
            "where LOWER(etiquetas.nombre) LIKE LOWER('%' || :texto || '%'))) ") // El nombre de la etiqueta
    fun obtenerTareasRetrasadasConFiltro(texto: String, estado: Estado = Estado.Retrasada): LiveData<List<Tarea>>



    // Obtiene toda las tareas proximas con el el filtro correspondiente
    @Transaction
    @Query("select * from tareas " + // obtener todo
            "where (fechaLimite > :fecha OR fechaLimite is null) " + // filtro fecha
            "AND estado = :estado " + // filtro estado"
            "AND (LOWER(nombre) like LOWER('%' || :texto || '%') " + // El nombre
            "OR LOWER(descripcion) like LOWER('%' || :texto || '%') " + // la descripcion
            "OR id IN (select idTarea from TareaEtiquetas " + // obtener datos de la subconsulta para el nombre de la etiqueta
            "join etiquetas on TareaEtiquetas.idEtiqueta = etiquetas.id " + // Comprobación de ids en la relacion many to many
            "where LOWER(etiquetas.nombre) LIKE LOWER('%' || :texto || '%'))) " + // El nombre de la etiqueta
            "ORDER BY fechaLimite ASC") // ordenación ascendente
    fun obtenerTareasProximasConFiltro(texto: String, fecha: Date = Date(), estado: Estado = Estado.EnTiempo) : LiveData<List<Tarea>>

    // Modificar una tarea en adapter
    @Update
    suspend fun modificarTarea(tarea: Tarea)

}