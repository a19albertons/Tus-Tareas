package com.example.tustareas.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.tustareas.repository.TusTareasRepository
import com.github.mikephil.charting.data.BarEntry

/**
 * Clase que gestiona  el submodelo de estadisticas
 */
class EstadisticasModel(private val repository: TusTareasRepository) {
    // Generación tercera grafica de estadisticas
    fun obtenerDatosGrafico(timestampDiasSemana: LongArray): LiveData<List<BarEntry>> {
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
    fun obtenerRueda(fechaInicio: Long, fechaFin: Long): LiveData<Pair<Long, Long>> {
        // variable base
        val resultado = MediatorLiveData<Pair<Long, Long>>()
        val completas = repository.estadisticas.obtenerCantidadTareasCompletasEntre2Fechas(fechaInicio, fechaFin)
        val noCompletas = repository.estadisticas.obtenerCantidadTareasPendientesEntre2Fechas(fechaInicio, fechaFin)

        // actualizador
        val valores = {
            val c = completas.value ?: 0
            val nc = noCompletas.value ?: 0
            resultado.value = Pair(c, nc)
        }

        // Observar
        resultado.addSource(completas) { valores() }
        resultado.addSource(noCompletas) { valores() }

        // resultado
        return resultado


    }
}