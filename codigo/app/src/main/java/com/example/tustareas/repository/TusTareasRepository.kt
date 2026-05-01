package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase

/**
 * Clase que representa al repositorio principal de la bd
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class TusTareasRepository(database: TusTareasDatabase) {
    // Imporaction daos
    private val activityMainConsultas = database.activityMainConsultas()


    // Todos los subrepositorios
    val inicio = InicioRepository(database)
    val verMas = VerMasRepository(database)
    val listarTareas = ListarTareasRepository(database)
    val tareaDetalles = TareaDetallesRepository(database)
    val modificarTareas = ModificarTareasRepository(database)
    val listarProyectos = ListarProyectosRepository(database)
    val proyectoDetalles = ProyectoDetallesRepository(database)
    val modificarProyectos = ModificarProyectosRepository(database)
    val estadisticas = EstadisticasRepository(database)
    val listarEtiquetas = ListarEtiquetasRepository(database)
    val etiquetaDetalles = EtiquetaDetallesRepository(database)
    val modificacionEtiqueta = ModificarEtiquetasRepository(database)



    // Metodos activity main
    /**
     * Elimina todas las tareas completadas de la base de datos
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun limpiarTareasCompletas() = activityMainConsultas.limpiarTareasCompletas()

    /**
     * Marca una notificación como leída en la base de datos
     *
     * @param idNotificacion El ID de la notificación a marcar como leída
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    suspend fun marcarNotificacionComoLeida(idNotificacion: Int) = activityMainConsultas.marcarNotificacionComoLeida(idNotificacion)




}