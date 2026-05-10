package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tustareas.repository.InicioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

/**
 * Clase que va representar el submodelo de tus tareas para el fragmento de inicio
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de inicio
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class InicioModel @Inject constructor(
    application: Application,
    val repository: InicioRepository
) : AndroidViewModel(application) {

    /**
     * Obtiene las tareas que terminan hoy
     *
     * @param fecha La fecha de hoy
     * @return Las tareas que terminan hoy
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasTerminanDiaEspecifico(fecha: Date) = repository.obtenerTareasTerminanDiaEspecifico(fecha)

    /**
     * Obtiene las tareas que terminan estan retrasadas
     *
     * @return Las tareas que terminan estan retrasadas
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasRetrasadas() = repository.obtenerTareasRetrasadas()

    /**
     * Obtiene las tareas que estan en tiempo y la fecha no esta excedida
     *
     * @param fecha La fecha de hoy
     * @return Las tareas que estan en tiempo y la fecha no esta excedida
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    fun obtenerTareasProximas(fecha: Date) = repository.obtenerTareasProximas(fecha)
}