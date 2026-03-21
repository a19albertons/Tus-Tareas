package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.modelos.Proyecto

@Dao
interface ProyectoConsultas {

    companion object {
        const val BASE_FILTRADO_PROYECTOS = "SELECT * FROM proyectos " + // Obtener todos los datos de los proyectos
                "where (LOWER(nombre) like LOWER('%' || :texto || '%') " + // Filtro nombre
                "OR LOWER(descripcion) like LOWER('%' || :texto || '%') " + // Filtro descripción
                "OR id IN (select idProyecto from ProyectoEtiquetas " + // Obtener datos de la subconsulta para el nombre de la etiqueta
                "join etiquetas on ProyectoEtiquetas.idEtiqueta = etiquetas.id " + // compacioón id de etiquetas
                "where LOWER(etiquetas.nombre) LIKE LOWER('%' || :texto || '%'))) " // Filtro etiquetas
    }
    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS")
    fun obtenerProyectosFiltradosPorDefecto(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin ASC")
    fun obtenerProyectosFiltradosPorFinAsc(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin DESC")
    fun obtenerProyectosFiltradosPorFinDes(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaInicio ASC")
    fun obtenerProyectosFiltradosPorInicioAsc(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin ASC, fechaInicio ASC")
    fun obtenerProyectosFiltradosPorInicioYFinAsc(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin ASC, fechaInicio DESC")
    fun obtenerProyectosFiltradosPorInicioAscYFinDes(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaInicio DESC")
    fun obtenerProyectosFiltradosPorInicioDes(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin DESC, fechaInicio ASC")
    fun obtenerProyectosFiltradosPorInicioDesYFinAsc(texto: String): LiveData<List<Proyecto>>

    @Transaction
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin DESC, fechaInicio DESC")
    fun obtenerProyectosFiltradosPorInicioYFinDes(texto: String): LiveData<List<Proyecto>>







}