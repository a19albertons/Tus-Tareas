package com.example.tustareas.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.adapters.VerMasAdapter
import com.example.tustareas.databinding.FragmentVerMasBinding
import com.example.tustareas.modelView.TusTareasModel


class VerMasFragment : Fragment() {
    private var _binding : FragmentVerMasBinding?= null
    private val binding : FragmentVerMasBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = {requireActivity()}
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVerMasBinding.inflate(inflater, container, false)
        val view = binding.root

        // args
        val args = VerMasFragmentArgs.fromBundle(requireArguments())
        val origen = args.numeroVerMas

        // configuracion adapter
        binding.listaTareasConCondicionesEnOrigen.layoutManager = LinearLayoutManager(requireContext())
        val adapter = VerMasAdapter(emptyList(), model, origen)
        binding.listaTareasConCondicionesEnOrigen.adapter = adapter

        // valores inicio
        binding.sinResultados.visibility = View.VISIBLE
        binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE

        // filtro copiado de otro lado
        val filtro = binding.filtro
        filtro.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(texto: CharSequence?, empieza: Int, posicion: Int, siguiente: Int) {

            }
            override fun onTextChanged(texto: CharSequence?, empieza: Int, fin: Int, posicion: Int) {

            }
            override fun afterTextChanged(texto: Editable?) {
                // Actualiza el texto del filtro como si fuese un observer unificado evita los dupliados que antes se generaban
                model.actualizarTextoVerMas(texto.toString())
            }

        })

        // consulta en función de origen
        when (origen) {
            1 -> {
                model.obtenerTareasTerminanDiaEspecificoConFiltro().observe(viewLifecycleOwner) {
                    tareas ->
                    if (tareas.isEmpty()) {
                        binding.sinResultados.visibility = View.VISIBLE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE
                    }
                    else  {
                        binding.sinResultados.visibility = View.GONE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.VISIBLE
                    }
                    adapter.submitList(tareas)
                }
            }
            2 -> {
                model.obtenerTareasRetrasadasConFiltro().observe(viewLifecycleOwner) {
                        tareas ->
                    if (tareas.isEmpty()) {
                        binding.sinResultados.visibility = View.VISIBLE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE
                    }
                    else {
                        binding.sinResultados.visibility = View.GONE
                        binding.listaTareasConCondicionesEnOrigen.visibility = View.VISIBLE
                    }
                    adapter.submitList(tareas)
                    }
            }
            3 -> model.obtenerTareasProximasConFiltro().observe(viewLifecycleOwner) {
                tareas ->
                if (tareas.isEmpty()) {
                    binding.sinResultados.visibility = View.VISIBLE
                    binding.listaTareasConCondicionesEnOrigen.visibility = View.GONE
                }
                else {
                    binding.sinResultados.visibility = View.GONE
                    binding.listaTareasConCondicionesEnOrigen.visibility = View.VISIBLE
                }
                adapter.submitList(tareas)
            }
        }

        return  view
    }


}