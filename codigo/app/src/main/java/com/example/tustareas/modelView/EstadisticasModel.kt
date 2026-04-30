package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.tustareas.repository.TusTareasRepository
import com.example.tustareas.util.DateHelper
import com.github.mikephil.charting.data.BarEntry
import java.util.Calendar
import java.util.TimeZone
import kotlin.div
import kotlin.text.toFloat
import kotlin.times

/**
 * Clase que gestiona  el submodelo de estadisticas
 */
class EstadisticasModel(private val repository: TusTareasRepository) {
    // Generación tercera grafica de estadisticas
    fun obtenerDatosGrafico(): LiveData<List<BarEntry>> {
        // Obtener timestamp de la semana
        val timestampDiasSemana = obtenerTimestampsDiasSemana()

        // variable base (mediadiador, completa, no completas)
        val resultado = MediatorLiveData<List<BarEntry>>()
        val completas = timestampDiasSemana.map { it -> repository.estadisticas.tareasCompletadasPorDia(it)  }
        val noCompletas = timestampDiasSemana.map { it -> repository.estadisticas.tareasNoCompletadasPorDia(it)  }

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

    // Funciones centrales de estadisticas
    fun obtenerCantidadTareasCompletas() = repository.estadisticas.obtenerCantidadTareasCompletas()
    fun obtenerCantidadTareasPendientes() = repository.estadisticas.obtenerCantidadTareasPendientes()
    fun obtenerCantidadTareasRetrasadas() = repository.estadisticas.obtenerCantidadTareasRetrasadas()



    // Generacion rueda (primer grafico)
    fun obtenerRueda(): LiveData<Float> {
        // Obtener timestamp de la semana
        val timestampDiasSemana = obtenerTimestampsDiasSemana()

        // Fechas de inicio y fin de la semana
        val fechaInicio = timestampDiasSemana.first()
        val fechaFin = timestampDiasSemana.last()


        // variable base
        val resultado = MediatorLiveData<Float>()
        val completas = repository.estadisticas.obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio, fechaFin)
        val noCompletas = repository.estadisticas.obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio, fechaFin)

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

    // Calculo de los timestamps de cada dia de la semana actual (lunes a domingo) con 00:00:00 en UTC y devuelve el array con los valores
    private fun obtenerTimestampsDiasSemana(): LongArray {
        // Configurar el calendar para que se situe en el lunes de la actual semana con 00:00:00 en UTC
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = DateHelper.fechaMediaNocheUTC()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val devolver = LongArray(7) {
            val timestampDia = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            timestampDia
        }
        return devolver
    }
    
}