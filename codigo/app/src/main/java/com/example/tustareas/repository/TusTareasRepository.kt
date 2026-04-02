package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase

class TusTareasRepository(database: TusTareasDatabase) {
    // Imporaction daos
    private val activityMainConsultas = database.activityMainConsultas()


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
    suspend fun limpiarTareasCompletas() = activityMainConsultas.limpiarTareasCompletas()




}