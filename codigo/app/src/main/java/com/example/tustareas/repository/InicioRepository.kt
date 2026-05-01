package com.example.tustareas.repository

import com.example.tustareas.db.TusTareasDatabase
import java.util.Date

/**
 * Clase que va representar el subrepositorio que hara las consultas contra el dao de inicio
 *
 * @param database La base de datos de la aplicación
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
class InicioRepository(database: TusTareasDatabase) {
    // Obtención del dao
    private val inicioConsultas = database.inicioConsultas()


    /**
     * Obtiene las tareas que terminan hoy
     *
     * @param fecha La fecha de hoy
     * @return Las tareas que terminan hoy
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = inicioConsultas.obtenerTareasTerminanDiaEspecifico(fecha)

    /**
     * Obtiene las tareas que terminan estan retrasadas
     *
     * @return Las tareas que terminan estan retrasadas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasRetrasadas() = inicioConsultas.obtenerTareasRetrasadas()

    /**
     * Obtiene las tareas que estan en tiempo y la fecha no esta excedida
     *
     * @param fecha La fecha de hoy
     * @return Las tareas que estan en tiempo y la fecha no esta excedida
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasProximas(fecha: Date) = inicioConsultas.obtenerTareasProximas(fecha)
}