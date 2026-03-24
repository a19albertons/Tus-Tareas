package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.tustareas.R
import com.example.tustareas.databinding.FragmentAjustesBinding
import com.example.tustareas.modelView.TusTareasModel


class AjustesFragment : Fragment() {
    private var _binding : FragmentAjustesBinding ?= null
    private val binding : FragmentAjustesBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = {requireActivity()}
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        val view = binding.root

        val idioma = listOf("Sistema", "Español", "Ingles", "Gallego")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            idioma
        )
        binding.idioma.adapter = adapter

        binding.idioma.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                posicion: Int,
                id: Long
            ) {
                when (posicion) {
                    // Actualizar idioma
                    0 -> model.setIdioma("Sistema")
                    1 -> model.setIdioma("Español")
                    2 -> model.setIdioma("Ingles")
                    3 -> model.setIdioma("Gallego")
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        return view
    }


}