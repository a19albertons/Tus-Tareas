package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import com.example.tustareas.modelos.Etiqueta

class TusTareasRepository(database: TusTareasDatabase) {
    // Imporaction daos
    private val etiquetaModificaciones = database.etiquetaModificaciones()
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




    // funcines daos





    // Insertar nueva etiqueta
    suspend fun insertarEtiqueta(etiqueta: Etiqueta) = etiquetaModificaciones.insertarEtiqueta(etiqueta)
    // Modificar etiqueta existente
    suspend fun modificarEtiqueta(etiqueta: Etiqueta) = etiquetaModificaciones.modificarEtiqueta(etiqueta)



    // Metodos activity main
    suspend fun limpiarTareasCompletas() = activityMainConsultas.limpiarTareasCompletas()




}