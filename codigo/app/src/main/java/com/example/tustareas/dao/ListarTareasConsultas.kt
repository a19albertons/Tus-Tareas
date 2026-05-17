package com.example.tustareas.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.modelos.Tarea

/**
 * Clase creada especificamente para poseer todas las operaciones sobre bd de consultas en listar tareas fragment
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@Dao
interface ListarTareasConsultas {
    // Creamos una constante con la consulta previamente usada
    companion object {
        const val BASE_FILTRADO_TAREAS =
            "select * from tareas " + // pedir datos
                "where prioridad IN (:prioridad) " + // filtro prioridad
                "AND estado IN (:estado) " + // filtro estado
                "AND (LOWER(nombre) like LOWER('%' || :textoTarea || '%') " + // filtro nombre tarea
                "OR LOWER(descripcion) like LOWER('%' || :textoTarea || '%') " + // filtro descripción tarea
                "OR id IN (select idTarea from TareaEtiquetas " + // obtener datos de la subconsulta para el nombre de la etiqueta
                "join etiquetas on TareaEtiquetas.idEtiqueta = etiquetas.id " + // Comprobación de ids
                "where LOWER(etiquetas.nombre) LIKE LOWER('%' || :textoTarea || '%'))) "
    }

    /**
     * Ordenacion fecha limite ascendente
     *
     * @param prioridad El array de prioridades a filtrar
     * @param estado El array de estados a filtrar
     * @param textoTarea El texto a filtrar en el nombre, descripción o etiquetas de la tarea
     * @return LiveData<List<Tarea>> devuelve una lista de tareas que cumplen con los filtros y ordenadas por fecha límite ascendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaLimite ASC")
    fun obtenerTareasFiltradasFechaLimiteAsc(
        prioridad: Array<Prioridad>,
        estado: Array<Estado>,
        textoTarea: String,
    ): LiveData<List<Tarea>>

    /**
     * Ordenacion fecha limite descendente
     *
     * @param prioridad El array de prioridades a filtrar
     * @param estado El array de estados a filtrar
     * @param textoTarea El texto a filtrar en el nombre, descripción o etiquetas de la tarea
     * @return LiveData<List<Tarea>> devuelve una lista de tareas que cumplen con los filtros y ordenadas por fecha límite descendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaLimite DESC")
    fun obtenerTareasFiltradasFechaLimiteDes(
        prioridad: Array<Prioridad>,
        estado: Array<Estado>,
        textoTarea: String,
    ): LiveData<List<Tarea>>

    /**
     * Ordenacion fecha creación ascendente
     *
     * @param prioridad El array de prioridades a filtrar
     * @param estado El array de estados a filtrar
     * @param textoTarea El texto a filtrar en el nombre, descripción o etiquetas de la tarea
     * @return LiveData<List<Tarea>> devuelve una lista de tareas que cumplen con los filtros y ordenadas por fecha creación ascendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaCreacion ASC")
    fun obtenerTareasFiltradasFechaCreacionAsc(
        prioridad: Array<Prioridad>,
        estado: Array<Estado>,
        textoTarea: String,
    ): LiveData<List<Tarea>>

    /**
     * Ordenacion fecha creación descendente
     *
     * @param prioridad El array de prioridades a filtrar
     * @param estado El array de estados a filtrar
     * @param textoTarea El texto a filtrar en el nombre, descripción o etiquetas de la tarea
     * @return LiveData<List<Tarea>> devuelve una lista de tareas que cumplen con los filtros y ordenadas por fecha creación descendente
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Query("$BASE_FILTRADO_TAREAS ORDER BY fechaCreacion DESC")
    fun obtenerTareasFiltradasFechaCreacionDes(
        prioridad: Array<Prioridad>,
        estado: Array<Estado>,
        textoTarea: String,
    ): LiveData<List<Tarea>>

    /**
     * Gestiona el cambio de estado de una tarea tras clickar en el checkbox de la tarea en base de datos
     *
     * @param tarea La tarea a modificar con el nuevo estado
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    @Update
    suspend fun modificarTarea(tarea: Tarea)
}
