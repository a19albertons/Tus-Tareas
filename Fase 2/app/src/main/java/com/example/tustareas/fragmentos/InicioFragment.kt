package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.TareasHoyPendientesAdapter
import com.example.tustareas.adapters.TareasProximasAdapter
import com.example.tustareas.adapters.TareasRetrasadasAdapter
import com.example.tustareas.dao.TareaConsultas
import com.example.tustareas.databinding.FragmentInicioBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Tarea
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
                0 -> binding.tareasHoyTexto.text = "No hay tareas pendientes para hoy"
                1 -> binding.tareasHoyTexto.text = "Tienes 1 tarea pendiente para hoy"
                else -> binding.tareasHoyTexto.text = "Tienes ${listadoTareasHoyFechaLimite?.size} tareas pendientes para hoy"
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
                0 -> binding.tareasRetrasadasTexto.text = "No hay tareas retrasadas"
                1 -> binding.tareasRetrasadasTexto.text = "Tienes 1 tarea retrasada"
                else -> binding.tareasRetrasadasTexto.text = "Tienes ${listadoTareasRetrasadas?.size} tareas retrasadas"
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
                0 -> binding.tareasProximasTexto.text = "No hay tareas próximas"
                1 -> binding.tareasProximasTexto.text = "Tienes 1 tarea próxima"
                else -> binding.tareasProximasTexto.text = "Tienes ${listadoTareasProximas?.size} tareas próximas"
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
            findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(1))
        }
        binding.verMas2.setOnClickListener {
            findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(2))
        }
        binding.verMas3.setOnClickListener {
            findNavController().navigate(InicioFragmentDirections.actionInicioFragmentToVerMasFragment(3))
        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}