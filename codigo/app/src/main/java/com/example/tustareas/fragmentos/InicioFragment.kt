package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.TareasHoyPendientesAdapter
import com.example.tustareas.adapters.TareasProximasAdapter
import com.example.tustareas.adapters.TareasRetrasadasAdapter
import com.example.tustareas.databinding.FragmentInicioBinding
import com.example.tustareas.modelView.InicioModel
import com.example.tustareas.util.DateHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

/**
 * Clase que gestiona el fragmento de inicio.
 *
 * @author Alberto Noceda <a19albertons@iessanclemen.net
 */
@AndroidEntryPoint
class InicioFragment : Fragment() {
    // Variables generales de la clase
    private var _binding: FragmentInicioBinding? = null
    val binding: FragmentInicioBinding
        get() = _binding!!

    val model: InicioModel by viewModels()

    private lateinit var hoy: Date

    // Variables a limpiar al matar el fragmento
    private var adapterProximas: TareasProximasAdapter? = null
    private var adapterRetrasadas: TareasRetrasadasAdapter? = null
    private var adapterHoy: TareasHoyPendientesAdapter? = null

    /**
     * Crea la vista del fragmento de inicio y gestiona los eventos de los elementos de la vista.
     *
     * @param inflater El inflador de la vista.
     * @param container El contenedor de la vista.
     * @param savedInstanceState El estado guardado de la vista.
     * @return La vista del fragmento de inicio.
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInicioBinding.inflate(inflater, container, false)

        // Definimos una única vez la fecha para las siguientes consultas, para evitar peticiones adicionales innecesarias internamente.
        hoy = DateHelper.fechaMediaNocheUTC()

        // Gestiona la sección de tareas para hoy
        gestionarTareasHoy()

        // Gestiona las tareas retrasadas
        gestionarTareasRetrasadas()

        // Gestiona las tareas próximas
        gestionarTareasProximas()

        // Gestión textos de ver más
        gestionarTextosVerMas()

        return binding.root
    }

    /**
     * Función privada que gestiona la sección de tareas para hoy. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarTareasHoy() {
        // scroll view
        // definir layout
        binding.tareasHoy.layoutManager = LinearLayoutManager(requireContext())

        // Definir adapter
        adapterHoy = TareasHoyPendientesAdapter()
        binding.tareasHoy.adapter = adapterHoy

        // De inicio oculto el ver más
        binding.verMas1.visibility = View.GONE

        // observar tareas pendientes para hoy
        model.obtenerTareasTerminanDiaEspecifico(hoy).observe(viewLifecycleOwner) { listadoTareasHoyFechaLimite ->
            // switch
            when (listadoTareasHoyFechaLimite?.size ?: 0) {
                0 -> binding.tareasHoyTexto.text = getString(R.string.no_hay_tareas_para_hoy)
                1 -> binding.tareasHoyTexto.text = getString(R.string.tienes_una_tarea_para_hoy)
                else -> binding.tareasHoyTexto.text = getString(R.string.tienes_tareas_para_hoy, listadoTareasHoyFechaLimite?.size)
            }

            // Asignar el adapter - diferenciamos si hay más o menos de 3 tareas
            if (listadoTareasHoyFechaLimite.size > 3) {
                adapterHoy!!.submitList(listadoTareasHoyFechaLimite.subList(0, 3))
                binding.verMas1.visibility = View.VISIBLE
            } else {
                adapterHoy!!.submitList(listadoTareasHoyFechaLimite)
                binding.verMas1.visibility = View.GONE
            }
        }
    }

    /**
     * Función privada que gestiona la sección de tareas retrasadas. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarTareasRetrasadas() {
        // Scroll view
        // definir layout
        binding.tareasRetrasadas.layoutManager = LinearLayoutManager(requireContext())

        // Definir adapter
        adapterRetrasadas = TareasRetrasadasAdapter()
        binding.tareasRetrasadas.adapter = adapterRetrasadas

        // De inicio oculto el ver más
        binding.verMas2.visibility = View.GONE

        // Gestiona la sección de tareas retrasadas
        model.obtenerTareasRetrasadas().observe(viewLifecycleOwner) { listadoTareasRetrasadas ->
            // switch
            when (listadoTareasRetrasadas?.size ?: 0) {
                0 -> binding.tareasRetrasadasTexto.text = getString(R.string.no_hay_tareas_retrasadas)
                1 -> binding.tareasRetrasadasTexto.text = getString(R.string.tienes_una_tarea_retrasada)
                else -> binding.tareasRetrasadasTexto.text = getString(R.string.tienes_tareas_retrasadas, listadoTareasRetrasadas?.size)
            }

            // Asignar el adapter
            if (listadoTareasRetrasadas.size > 3) {
                adapterRetrasadas!!.submitList(listadoTareasRetrasadas.subList(0, 3))
                binding.verMas2.visibility = View.VISIBLE
            } else {
                adapterRetrasadas!!.submitList(listadoTareasRetrasadas)
                binding.verMas2.visibility = View.GONE
            }
        }
    }

    /**
     * Función privada que gestiona la sección de tareas próximas. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarTareasProximas() {
        // scroll view
        // definir layout
        binding.tareasProximas.layoutManager = LinearLayoutManager(requireContext())

        // Definir adapter
        adapterProximas = TareasProximasAdapter()
        binding.tareasProximas.adapter = adapterProximas

        // De inicio oculto el ver más
        binding.verMas3.visibility = View.GONE

        // Gestiona las tareas futuras
        model.obtenerTareasProximas(hoy).observe(viewLifecycleOwner) { listadoTareasProximas ->
            // switch
            when (listadoTareasProximas?.size ?: 0) {
                0 -> binding.tareasProximasTexto.text = getString(R.string.no_hay_tareas_proximas)
                1 -> binding.tareasProximasTexto.text = getString(R.string.tienes_una_tarea_proxima)
                else -> binding.tareasProximasTexto.text = getString(R.string.tienes_tareas_proximas, listadoTareasProximas?.size)
            }

            // Asignar el adapter
            if (listadoTareasProximas.size > 3) {
                adapterProximas!!.submitList(listadoTareasProximas.subList(0, 3))
                binding.verMas3.visibility = View.VISIBLE
            } else {
                adapterProximas!!.submitList(listadoTareasProximas)
                binding.verMas3.visibility = View.GONE
            }
        }
    }

    /**
     * Función privada que gestiona los textos de ver más. Su misión es reducir el llamado código spaguetti que había en onCreateView y mejorar la legibilidad del código.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    private fun gestionarTextosVerMas() {
        // Textos ver más
        // Gestiona el ver más de tareas para hoy
        binding.verMas1.setOnClickListener {
            try {
                findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(1))
            } catch (_: Exception) {
                Snackbar
                    .make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        // Gestiona el ver más de tareas retrasadas
        binding.verMas2.setOnClickListener {
            try {
                findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(2))
            } catch (_: Exception) {
                Snackbar
                    .make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        // Gestiona el ver más de tareas próximas
        binding.verMas3.setOnClickListener {
            try {
                findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(3))
            } catch (_: Exception) {
                Snackbar
                    .make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * Destruye la vista del fragmento de inicio y libera los recursos asociados a la vista.
     *
     * @author Alberto Noceda <a19albertons@iessanclemente.net>
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // liberar recursos
        adapterHoy = null
        adapterRetrasadas = null
        adapterProximas = null
    }
}
