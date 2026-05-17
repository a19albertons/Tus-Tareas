package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentEstadisticasBinding
import com.example.tustareas.modelView.EstadisticasModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que gestiona el fragmento de estadísticas.
 *
 * @author Alberto Noceda <a19albertons@iessanclemente.net>
 */
@AndroidEntryPoint
class EstadisticasFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentEstadisticasBinding? = null
    private val binding: FragmentEstadisticasBinding
        get() = _binding!!

    val model: EstadisticasModel by viewModels()

    /**
     * Crea la vista del fragmento estadísticas y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento estadísticas.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)

        // Gestion de la estadistica semanal
        gestionEstadisticaSemanal()

        // Gestion de los datos centrales
        gestionDatosCentrales()

        // Gestion del grafico en forma de rueda
        gestionGraficoRueda()

        return binding.root
    }

    /**
     * Funcion privada que genera la estadística semanal. Su mision es reducir el llamado codigo spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionEstadisticaSemanal() {
        // Generamos el tercer grafico
        model.obtenerDatosGrafico().observe(viewLifecycleOwner) { resultados ->
            // Creamos el dataset de barras
            val dataSet = BarDataSet(resultados, "")

            // Configuramos los colore de cada barra (completada, no completadas
            dataSet.colors =
                listOf(
                    resources.getColor(R.color.blueStats, null),
                    resources.getColor(R.color.grayStatsToolbar, null),
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
            binding.grafico.axisLeft.isEnabled = false // Oculta numeros de la izquierda
            binding.grafico.axisRight.isEnabled = false // Oculta numero de la derecha

            // Añadimos el bar data y forzamos recarga
            binding.grafico.data = barData
            binding.grafico.invalidate()
        }
    }

    /**
     * Funcion privada que gestiona los datos centrales. Su mision es reducir el llamado codigo spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionDatosCentrales() {
        // Actualizamos los datos centrales
        // Actualizar cantidad completas
        model.obtenerCantidadTareasCompletas().observe(viewLifecycleOwner) { cantidad ->
            binding.completas.text = cantidad.toString()
        }

        // Actualizar cantidad pendientes
        model.obtenerCantidadTareasPendientes().observe(viewLifecycleOwner) { cantidad ->
            binding.pendiente.text = cantidad.toString()
        }

        // Actualizar cantidad retrasadas
        model.obtenerCantidadTareasRetrasadas().observe(viewLifecycleOwner) { cantidad ->
            binding.retrasadas.text = cantidad.toString()
        }
    }

    /**
     * Funcion privada que gestiona el grafico en forma de rueda. Su mision es reducir el llamado codigo spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionGraficoRueda() {
        // Obtiene el primer grafico
        model.obtenerRueda().observe(viewLifecycleOwner) { progreso ->
            binding.graficoRedondo.progress = progreso.toInt()
            binding.graficoRedondoTexto.text = progreso.toInt().toString()
        }
    }

    /**
     * Destruye la vista del fragmento estadísticas y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
