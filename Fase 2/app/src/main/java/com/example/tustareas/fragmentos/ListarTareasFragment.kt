package com.example.tustareas.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.adapters.TareasAdapter
import com.example.tustareas.databinding.FragmentListarTareasBinding
import com.example.tustareas.modelView.TusTareasModel

class ListarTareasFragment : Fragment() {
    private var _binding: FragmentListarTareasBinding? = null
    private val binding: FragmentListarTareasBinding
            get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListarTareasBinding.inflate(inflater, container, false)
        val view = binding.root

        // Obtener recycler view
        val recyclerView = binding.listaTareas

        // Definir el layout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Definir el adapter
        model.obtenerTodasLasTareas().observe(viewLifecycleOwner) {
            listaTareas ->
            recyclerView.adapter = TareasAdapter(listaTareas, model)
            if (listaTareas.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
            else  {
                binding.sinResultados.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}