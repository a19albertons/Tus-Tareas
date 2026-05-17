package com.example.tustareas.modelView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.tustareas.repository.EstadisticasRepository
import com.example.tustareas.util.DateHelper
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

/**
 * Clase que gestiona  el submodelo de estadisticas
 *+
 *
 * @constructor Crea un constructor para ser usado por el propio Hilt e inyectar las dependencias automáticamente
 * @param application el application de Android, necesario para el ViewModel
 * @param repository El repositorio de estadísticas.
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@HiltViewModel
class EstadisticasModel
    @Inject
    constructor(
        application: Application,
        private val repository: EstadisticasRepository,
    ) : AndroidViewModel(application) {
        // Generación tercera grafica de estadisticas

        /**
         * Obtiene los datos necesarios para generar el gráfico de barras que muestre la cantidad de
         * tareas completas y no completas por dia en la actual semana.
         *
         * @return Un LiveData que contiene una lista de BarEntry, donde cada BarEntry representa
         * un dia con ambos valores (completas y no completas) para ese dia.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerDatosGrafico(): LiveData<List<BarEntry>> {
            // Obtener timestamp de la semana
            val timestampDiasSemana = obtenerTimestampsDiasSemana()

            // variable base (mediadiador, completa, no completas)
            val resultado = MediatorLiveData<List<BarEntry>>()
            val completas = timestampDiasSemana.map { it -> repository.tareasCompletadasPorDia(it) }
            val noCompletas = timestampDiasSemana.map { it -> repository.tareasNoCompletadasPorDia(it) }

            // Generación dataset
            val nuevoDataset = {
                // variable de datos
                val entradas = ArrayList<BarEntry>()
                // Bucle for metiendole los datos al resultado
                for (i in timestampDiasSemana.indices) {
                    entradas.add(BarEntry(i.toFloat(), floatArrayOf(completas[i].value ?: 0f, noCompletas[i].value ?: 0f)))
                }
                // devuelve el resultado
                resultado.value = entradas
            }

            // Observers
            completas.forEach { resultado.addSource(it) { nuevoDataset() } }
            noCompletas.forEach { resultado.addSource(it) { nuevoDataset() } }

            // Devolvemos el dataset
            return resultado
        }

        /**
         * Obtiene la cantidad de tareas completadas a lo largo del tiempo.
         *
         * @return Un LiveData que contiene un Long con la cantidad total de tareas completadas.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerCantidadTareasCompletas() = repository.obtenerCantidadTareasCompletas()

        /**
         * Obtiene la cantidad de tareas pendientes a lo largo del tiempo.
         *
         * @return Un LiveData que contiene un Long con la cantidad total de tareas pendientes.
         * @author Alberto Noceda <a19albertons@iessanclement
         */
        fun obtenerCantidadTareasPendientes() = repository.obtenerCantidadTareasPendientes()

        /**
         * Obtiene la cantidad de tareas retrasadas a lo largo del tiempo.
         *
         * @return Un LiveData que contiene un Long con la cantidad total de tareas retrasadas.
         * @author Alberto Noceda <a19albertons@iessanclement
         */
        fun obtenerCantidadTareasRetrasadas() = repository.obtenerCantidadTareasRetrasadas()

        /**
         * Obtiene el porcentaje de tareas completadas en la semana actual, calculado a partir de la
         * cantidad de tareas completadas y pendientes entre las fechas de inicio y fin de la semana.
         *
         * @return Un LiveData que contiene un Float con el porcentaje de tareas completadas en la semana actual,
         * calculado a partir de la cantidad de tareas completadas y pendientes entre las fechas de inicio y fin de la semana.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        fun obtenerRueda(): LiveData<Float> {
            // Obtener timestamp de la semana
            val timestampDiasSemana = obtenerTimestampsDiasSemana()

            // Fechas de inicio y fin de la semana
            val fechaInicio = timestampDiasSemana.first()
            val fechaFin = timestampDiasSemana.last()

            // variable base
            val resultado = MediatorLiveData<Float>()
            val completas = repository.obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio, fechaFin)
            val noCompletas = repository.obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio, fechaFin)

            // actualizador
            val valores = {
                val c = completas.value ?: 0L
                val nc = noCompletas.value ?: 0L
                val total = c + nc
                if (total == 0L) {
                    resultado.value = 0f
                } else {
                    resultado.value = c.toFloat() / total.toFloat() * 100
                }
            }

            // Observar
            resultado.addSource(completas) { valores() }
            resultado.addSource(noCompletas) { valores() }

            return resultado
        }

        /**
         * Obtiene los timestamps de cada dia de la semana actual (lunes a domingo) con 00:00:00 en UTC y devuelve el array con los valores.
         *
         * @return Un LongArray que contiene los timestamps de cada dia de la semana actual, comenzando por el lunes y terminando por el domingo, con 00:00:00 en UTC.
         * @author Alberto Noceda <a19albertons@iessanclemente.net>
         */
        private fun obtenerTimestampsDiasSemana(): LongArray {
            // Configurar el calendar para que se situe en el lunes de la actual semana con 00:00:00 en UTC
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = DateHelper.fechaMediaNocheUTC()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

            val devolver =
                LongArray(7) {
                    val timestampDia = calendar.timeInMillis
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    timestampDia
                }
            return devolver
        }
    }
