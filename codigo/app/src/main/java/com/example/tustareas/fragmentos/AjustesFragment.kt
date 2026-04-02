package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        val view = binding.root

        val idioma = resources.getStringArray(R.array.idiomas)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            idioma
        )
        binding.idioma.adapter = adapter

        // Obtenemos el valor por defecto del modelo y lo deshabilitamos en la primera
        // ejecucíón para evitar un reinicio (recreación
        val idiomaGuardado = model.idioma.value
        val posicionInicial = when(idiomaGuardado) {
            "Español" -> 1
            "Ingles" -> 2
            "Gallego" -> 3
            else -> 0
        }
        binding.idioma.setSelection(posicionInicial, false)

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
        // Radio Buttons
        binding.claro.setOnClickListener {
            model.setTema(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding.oscuro.setOnClickListener {
            model.setTema(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding.sistema.setOnClickListener {
            model.setTema(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        // Vigila que el clickado sea el unico marcado y desmarcar el resto
        model.tema.observe(viewLifecycleOwner) { modo ->
            binding.claro.isChecked = (modo == AppCompatDelegate.MODE_NIGHT_NO)
            binding.oscuro.isChecked = (modo == AppCompatDelegate.MODE_NIGHT_YES)
            binding.sistema.isChecked = (modo == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }


        return view
    }


}