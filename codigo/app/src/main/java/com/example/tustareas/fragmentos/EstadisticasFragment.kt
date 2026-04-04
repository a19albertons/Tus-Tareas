package com.example.tustareas.fragmentos

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentEstadisticasBinding
import com.example.tustareas.modelView.TusTareasModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Date

/**
 * Clase que gestiona el fragmento de estadísticas.
 */
class EstadisticasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding : FragmentEstadisticasBinding? = null
    private val binding : FragmentEstadisticasBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        val view = binding.root

        // Configurar el calendar para que se situe en el lunes de la actual semana con 00:00:00
        val calendar = Calendar.getInstance()
        val timestamp = Date()
        calendar.time = timestamp
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Obtener los timestamps de los días de la semana de lunes a domingo
        val timestampDiasSemana = LongArray(7) {
            val timestampDia = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            timestampDia
        }

        // Generamos el tercer grafico
        model.estadisticas.obtenerDatosGrafico(timestampDiasSemana).observe(viewLifecycleOwner) {
            resultados ->
            // Creamos el dataset de barras
            val dataSet = BarDataSet(resultados, "")

            // Configuramos los colore de cada barra (completada, no completadas
            dataSet.colors = listOf(
                resources.getColor(R.color.blueStats, null),
                resources.getColor(R.color.grayStatsToolbar, null)
            )

            // Customizamos el grosor de las barras y configuramos el bar data con el dataset
            val barData = BarData(dataSet)
            barData.barWidth = 0.4f

            // Definimos el eje x de las letras
            // Creamos el eje inferior de las letras
            val xAxis = binding.grafico.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            // Definimos las siglas de los dias de la semana
            val dias = resources.getStringArray(R.array.dias_semana_siglas)
            xAxis.valueFormatter = IndexAxisValueFormatter(dias)
            xAxis.granularity = 1f // Establece el tamaño de que se muestren

            // valores que especificamos
            dataSet.setDrawValues(false) // Elimina algunos numeros de las barras (leyenda)
            xAxis.setDrawGridLines(false) // Quita lineas zona vertical
            xAxis.setDrawAxisLine(false) // Quita lineas zona horizontal
            binding.grafico.description.isEnabled = false // deshabilita la descripcion
            binding.grafico.legend.isEnabled = false // deshabilita la leyenda
            binding.grafico.setTouchEnabled(false) // Deshabilita iteraciones
            binding.grafico.axisLeft.isEnabled = false  // Oculta numeros de la izquierda
            binding.grafico.axisRight.isEnabled = false // Oculta numero de la derecha

            // Añadimos el bar data y forzamos recarga
            binding.grafico.data = barData
            binding.grafico.invalidate()
        }

        // Actualizamos los datos centrales
        // Actualizar cantidad completas
        model.estadisticas.obtenerCantidadTareasCompletas().observe(viewLifecycleOwner) {
            cantidad ->
            binding.completas.text = cantidad.toString()
        }

        // Actualizar cantidad pendientes
        model.estadisticas.obtenerCantidadTareasPendientes().observe(viewLifecycleOwner) {
            cantidad ->
            binding.pendiente.text = cantidad.toString()
        }

        // Actualizar cantidad retrasadas
        model.estadisticas.obtenerCantidadTareasRetrasadas().observe(viewLifecycleOwner) {
            cantidad ->
            binding.retrasadas.text = cantidad.toString()
        }

        // Primer grafico
        val fechaInicio = timestampDiasSemana.first()
        val fechaFin = timestampDiasSemana.last()


        // Obtiene el primer grafico
        model.estadisticas.obtenerRueda(fechaInicio, fechaFin).observe(viewLifecycleOwner) {
            valores ->
            val completas = valores.first
            val pendientes = valores.second
            val progreso = completas.toFloat() / (completas + pendientes).toFloat() * 100
            binding.graficoRedondo.progress = progreso.toInt()
            binding.graficoRedondoTexto.text = progreso.toInt().toString()
        }





        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}