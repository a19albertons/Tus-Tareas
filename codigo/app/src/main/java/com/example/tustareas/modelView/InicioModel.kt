package com.example.tustareas.modelView

import com.example.tustareas.repository.TusTareasRepository
import java.util.Date

/**
 * Clase que va representar el submodelo de tus tareas para el fragmento de inicio
 *
 * @param repository Repositorio de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class InicioModel(private val repository: TusTareasRepository) {
    // Llama al repositorio para obtener las tareas debidas en cada caso

    /**
     * Obtiene las tareas que terminan hoy
     *
     * @param fecha La fecha de hoy
     * @return Las tareas que terminan hoy
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = repository.inicio.obtenerTareasTerminanDiaEspecifico(fecha)

    /**
     * Obtiene las tareas que terminan estan retrasadas
     *
     * @return Las tareas que terminan estan retrasadas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasRetrasadas() = repository.inicio.obtenerTareasRetrasadas()

    /**
     * Obtiene las tareas que estan en tiempo y la fecha no esta excedida
     *
     * @param fecha La fecha de hoy
     * @return Las tareas que estan en tiempo y la fecha no esta excedida
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasProximas(fecha: Date) = repository.inicio.obtenerTareasProximas(fecha)
}