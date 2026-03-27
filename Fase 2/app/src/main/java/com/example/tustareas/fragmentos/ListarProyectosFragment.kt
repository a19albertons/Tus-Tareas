package com.example.tustareas.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.ProyectosAdapter
import com.example.tustareas.databinding.FragmentListarProyectosBinding
import com.example.tustareas.dto.ProyectoDTO
import com.example.tustareas.filtros.OrdenarProyectoFin
import com.example.tustareas.filtros.OrdenarProyectosInicio
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Proyecto
import com.google.android.material.snackbar.Snackbar
import java.util.Date


class ListarProyectosFragment : Fragment() {
    private var _binding : FragmentListarProyectosBinding ?= null
    private val binding : FragmentListarProyectosBinding
        get() = _binding!!

    val model : TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListarProyectosBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.listaProyectos.layoutManager = LinearLayoutManager(requireContext())

        // Observador
        model.obtenerProyectosFiltradas().observe(viewLifecycleOwner) {
            listadoProyectos ->
            if (listadoProyectos.isEmpty()) {
                binding.sinResultados.visibility = View.VISIBLE
                binding.listaProyectos.visibility = View.GONE
            }
            else {
                binding.sinResultados.visibility = View.GONE
                binding.listaProyectos.visibility = View.VISIBLE

            }
            binding.listaProyectos.adapter = ProyectosAdapter(listadoProyectos)
        }

        // Control filtro texto
        binding.filtro.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(texto: Editable?) {
                model.actualizarTextoListadoProyectos(texto.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        // Filtro por fecha inicio
        val listaInicio = listOf(
            getString(R.string.inicio),
            getString(R.string.ascendente),
            getString(R.string.descendente)
        )
        binding.inicioProyecto.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listaInicio
        )
        binding.inicioProyecto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                posicion: Int,
                id: Long
            ) {
                when (posicion) {
                    0 -> model.actualizarInicioProyecto(OrdenarProyectosInicio.INICIO)
                    1 -> model.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_ASC)
                    2 -> model.actualizarInicioProyecto(OrdenarProyectosInicio.FECHA_DES)
                }
                // Notificación de filtros
                if (binding.finProyecto.selectedItemPosition != 0 && binding.inicioProyecto.selectedItemPosition != 0) {
                    Snackbar.make(binding.root, getString(R.string.primero_va_fin_despues_inicio), Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Filtro por fecha fin
        val listaFin = listOf(
            getString(R.string.fin),
            getString(R.string.ascendente),
            getString(R.string.descendente)
        )
        binding.finProyecto.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listaFin
        )
        binding.finProyecto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                posicion: Int,
                id: Long
            ) {
                when (posicion) {
                    0 -> model.actualizarFinProyecto(OrdenarProyectoFin.FIN)
                    1 -> model.actualizarFinProyecto(OrdenarProyectoFin.FECHA_ASC)
                    2 -> model.actualizarFinProyecto(OrdenarProyectoFin.FECHA_DES)
                }
                // Notificación de filtros
                if (binding.finProyecto.selectedItemPosition != 0 && binding.inicioProyecto.selectedItemPosition != 0) {
                    Snackbar.make(binding.root, getString(R.string.primero_va_fin_despues_inicio), Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Boton añadir
        binding.anadirProyecto.setOnClickListener {
            // Mandamos un valor de proyectoDTO totalmente vacio con valores por defecto
            val proyectoDTO = ProyectoDTO(
                proyecto = Proyecto(0,"", "", Date(), null, null),
                etiquetas = emptyList(),
                tareas = emptyList()
            )
            try {
                findNavController().navigate(ListarProyectosFragmentDirections.actionListarProyectosFragmentToModificarProyectoFragment(proyectoDTO))
            }
            catch (e: Exception) {
                Snackbar.make(binding.root, getString(R.string.error_navegar), Snackbar.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}