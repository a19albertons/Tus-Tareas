package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tustareas.modelos.Proyecto

/**
 * Clase que gestiona las consultas contra la bd de listar proyectos
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ListarProyectosConsultas {
    // Variable base de filtrado
    companion object {
        const val BASE_FILTRADO_PROYECTOS = "SELECT * FROM proyectos " + // Obtener todos los datos de los proyectos
                "where (LOWER(nombre) like LOWER('%' || :texto || '%') " + // Filtro nombre
                "OR LOWER(descripcion) like LOWER('%' || :texto || '%') " + // Filtro descripción
                "OR id IN (select idProyecto from ProyectoEtiquetas " + // Obtener datos de la subconsulta para el nombre de la etiqueta
                "join etiquetas on ProyectoEtiquetas.idEtiqueta = etiquetas.id " + // compacioón id de etiquetas
                "where LOWER(etiquetas.nombre) LIKE LOWER('%' || :texto || '%'))) " // Filtro etiquetas
    }
    /**
     * Obtiene los proyectos filtrados por el texto sin ordenación específica
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS")
    fun obtenerProyectosFiltradosPorDefecto(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de fin ascendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de fin ascendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin ASC")
    fun obtenerProyectosFiltradosPorFinAsc(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de fin descendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de fin descendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin DESC")
    fun obtenerProyectosFiltradosPorFinDes(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de inicio ascendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de inicio ascendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaInicio ASC")
    fun obtenerProyectosFiltradosPorInicioAsc(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de fin ascendente y fecha de inicio ascendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de fin ascendente y fecha de inicio ascendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin ASC, fechaInicio ASC")
    fun obtenerProyectosFiltradosPorInicioYFinAsc(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de fin descendente y fecha de inicio ascendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de fin descendente y fecha de inicio ascendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin DESC, fechaInicio ASC")
    fun obtenerProyectosFiltradosPorInicioAscYFinDes(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de inicio descendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de inicio descendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaInicio DESC")
    fun obtenerProyectosFiltradosPorInicioDes(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de fin ascendente y fecha de inicio descendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de fin ascendente y fecha de inicio descendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin ASC, fechaInicio DESC")
    fun obtenerProyectosFiltradosPorInicioDesYFinAsc(texto: String): LiveData<List<Proyecto>>

    /**
     * Obtiene los proyectos filtrados por el texto y ordenados por fecha de fin descendente y fecha de inicio descendente
     *
     * @param texto El texto a filtrar
     * @return LiveData<List<Proyecto>> devuelve una lista de proyectos que cumplen con el filtro y ordenados por fecha de fin descendente y fecha de inicio descendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_PROYECTOS ORDER BY fechaFin DESC, fechaInicio DESC")
    fun obtenerProyectosFiltradosPorInicioYFinDes(texto: String): LiveData<List<Proyecto>>
}