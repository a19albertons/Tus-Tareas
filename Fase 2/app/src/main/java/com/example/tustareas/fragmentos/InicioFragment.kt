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
import com.example.tustareas.modelView.TusTareasModel
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class InicioFragment : Fragment() {
    private var _binding: FragmentInicioBinding? = null
    private val binding: FragmentInicioBinding
        get() = _binding!!
    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val view = binding.root

        // observar tareas pendientes para hoy
        model.obtenerTareasTerminanDiaEspecifico(Date()).observe(viewLifecycleOwner) {
            listadoTareasHoyFechaLimite ->
            // switch
            when (listadoTareasHoyFechaLimite?.size ?: 0) {
                0 -> binding.tareasHoyTexto.text = getString(R.string.no_hay_tareas_para_hoy)
                1 -> binding.tareasHoyTexto.text = getString(R.string.tienes_una_tarea_para_hoy)
                else -> binding.tareasHoyTexto.text = getString(R.string.tienes_tareas_para_hoy, listadoTareasHoyFechaLimite?.size)
            }

            // scroll view
            // obtener referencia
            val primerRecyclerView = binding.tareasHoy

            // definir layout
            primerRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Asignar el adapter - diferenciamos si hay más o menos de 3 tareas
            if (listadoTareasHoyFechaLimite.size > 3) {
                primerRecyclerView.adapter = TareasHoyPendientesAdapter(listadoTareasHoyFechaLimite.subList(0,3))
                binding.verMas1.visibility = View.VISIBLE
            }
            else {
                primerRecyclerView.adapter = TareasHoyPendientesAdapter(listadoTareasHoyFechaLimite)
                binding.verMas1.visibility = View.GONE
            }
        }

        model.obtenerTareasRetrasadas(Date()).observe(viewLifecycleOwner) {
            listadoTareasRetrasadas ->
            // switch
            when (listadoTareasRetrasadas?.size ?: 0) {
                0 -> binding.tareasRetrasadasTexto.text = getString(R.string.no_hay_tareas_retrasadas)
                1 -> binding.tareasRetrasadasTexto.text = getString(R.string.tienes_una_tarea_retrasada)
                else -> binding.tareasRetrasadasTexto.text = getString(R.string.tienes_tareas_retrasadas, listadoTareasRetrasadas?.size)
            }

            // Scroll view
            // obtener referencia
            val segundoRecyclerView = binding.tareasRetrasadas

            // definir layout
            segundoRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Asignar el adapter
            if (listadoTareasRetrasadas.size > 3) {
                segundoRecyclerView.adapter =
                    TareasRetrasadasAdapter(listadoTareasRetrasadas.subList(0, 3))
                binding.verMas2.visibility = View.VISIBLE
            }
            else {
                segundoRecyclerView.adapter = TareasRetrasadasAdapter(listadoTareasRetrasadas)
                binding.verMas2.visibility = View.GONE
            }
        }

        model.obtenerTareasProximas(Date()).observe(viewLifecycleOwner) {
            listadoTareasProximas ->
            // switch
            when (listadoTareasProximas?.size ?: 0) {
                0 -> binding.tareasProximasTexto.text = getString(R.string.no_hay_tareas_proximas)
                1 -> binding.tareasProximasTexto.text = getString(R.string.tienes_una_tarea_proxima)
                else -> binding.tareasProximasTexto.text = getString(R.string.tienes_tareas_proximas, listadoTareasProximas?.size)
            }

            // scroll view
            // obtener referencia
            val terceroRecyclerView = binding.tareasProximas

            // definir layout
            terceroRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Asignar el adapter
            if (listadoTareasProximas.size > 3) {
                terceroRecyclerView.adapter =
                    TareasProximasAdapter(listadoTareasProximas.subList(0, 3))
                binding.verMas3.visibility = View.VISIBLE
            }
            else {
                terceroRecyclerView.adapter = TareasProximasAdapter(listadoTareasProximas)
                binding.verMas3.visibility = View.GONE
            }

        }

        // Textos ver más
        binding.verMas1.setOnClickListener {
            try {
                findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(1))
            } catch (e: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.verMas2.setOnClickListener {
            try {
                findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(2))
            }
            catch (e: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.verMas3.setOnClickListener {
            try {
                findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(3))
            }
            catch (e: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}