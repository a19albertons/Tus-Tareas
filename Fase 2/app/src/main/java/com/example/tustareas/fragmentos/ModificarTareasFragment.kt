package com.example.tustareas.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tustareas.R
import com.example.tustareas.adapters.ListaEtiquetasPresentesAdapter
import com.example.tustareas.databinding.FragmentModificarTareasBinding
import com.example.tustareas.modelView.TusTareasModel
import com.example.tustareas.modelos.Estado
import com.example.tustareas.modelos.Etiqueta
import com.example.tustareas.modelos.Prioridad
import com.example.tustareas.util.DateHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.util.Date


class ModificarTareasFragment : Fragment() {
    private var _binding: FragmentModificarTareasBinding? = null
    private val binding: FragmentModificarTareasBinding
        get() = _binding!!

    val model: TusTareasModel by viewModels(
        ownerProducer = { this.requireActivity() }
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentModificarTareasBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = ModificarTareasFragmentArgs.fromBundle(requireArguments())
        val tareaDTO = args.tareaDTO

        // Valores del dto
        binding.tituloTarea.setText(tareaDTO.tarea.nombre)
        binding.descipcionTarea.setText(tareaDTO.tarea.descripcion)
        binding.fechaCreacionTarea.text = DateHelper.timestampToString(tareaDTO.tarea.fechaCreacion)
        binding.fechaLimiteTarea.text = DateHelper.timestampToString(tareaDTO.tarea.fechaLimite)
        binding.prioridadTarea.setSelection(tareaDTO.tarea.prioridad.ordinal)
        binding.estadoTarea.setSelection(tareaDTO.tarea.estado.ordinal)

        // Spinner prioridad
        val contenidosSpinerPrioridad = listOf("Alta", "Media", "Baja", "No establecida")
        binding.prioridadTarea.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            contenidosSpinerPrioridad
        )
        binding.prioridadTarea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0-> {
                        binding.prioridadTarea.setSelection(0)
                        tareaDTO.tarea.prioridad = Prioridad.Alta
                    }
                    1-> {
                        binding.prioridadTarea.setSelection(1)
                        tareaDTO.tarea.prioridad = Prioridad.Media
                    }
                    2-> {
                        binding.prioridadTarea.setSelection(2)
                        tareaDTO.tarea.prioridad = Prioridad.Baja
                    }
                    3-> {
                        binding.prioridadTarea.setSelection(3)
                        tareaDTO.tarea.prioridad = Prioridad.NoEstablecido
                    }
                    else -> {
                        binding.prioridadTarea.setSelection(0)
                        tareaDTO.tarea.prioridad = Prioridad.Alta
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // spinner de estado
        val contenidosSpinerEstado = listOf("En tiempo", "Retrasado", "Completada")
        binding.estadoTarea.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            contenidosSpinerEstado
        )
        binding.estadoTarea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0-> {
                        tareaDTO.tarea.estado = Estado.EnTiempo
                    }
                    1-> {
                        tareaDTO.tarea.estado = Estado.Retrasada
                    }
                    2-> {
                        tareaDTO.tarea.estado = Estado.Completada
                    }
                    else -> {
                        tareaDTO.tarea.estado = Estado.EnTiempo
                    }

                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // calendario de fecha limite. No es como en el diseño de Figma
        binding.calendario.setOnClickListener {
            // Creamos una instancia de MaterialDatePicker
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Fecha limite")
            // Construimos el datePicker
            val picker = builder.build()

            // Customizamos el boton de confirmar
            picker.addOnPositiveButtonClickListener { eleccion ->

                val FechaEscogidaPorUsuario = Date(eleccion)
                tareaDTO.tarea.fechaLimite = FechaEscogidaPorUsuario
                binding.fechaLimiteTarea.text = DateHelper.timestampToString(FechaEscogidaPorUsuario)
            }

            // Mostramos el datePicker
            picker.show(parentFragmentManager, "escoger fecha")
        }

        // valor por defecto vacio
        var listaEtiquetas = listOf(Etiqueta(0, "No existen etiquetas"))
        binding.listaEtiquetas.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            // Muestra solo el nombre, pero internamente es la clase
            listaEtiquetas.map { it.nombre}
        )
        // Gestionar etiqueta
        model.obtenerEtiquetasRestantes().observe(viewLifecycleOwner) { etiquetas ->
            if (etiquetas.isEmpty()) {
                listaEtiquetas = listOf(Etiqueta(0, "No existen etiquetas"))
            }
            else {
                listaEtiquetas = etiquetas

            }
            binding.listaEtiquetas.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listaEtiquetas.map { it.nombre }
            )
        }

        // Recycler View etiquetas presentes
        val adapter = ListaEtiquetasPresentesAdapter {
            listaEtiquetas ->
            tareaDTO.etiquetas = listaEtiquetas
            model.actualizarFiltroListaEtiquetaTareas(tareaDTO.etiquetas)
        }
        binding.recyclerViewMostrarEtiquetas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMostrarEtiquetas.adapter = adapter
        adapter.submitList(tareaDTO.etiquetas.toList())



        // Boton añadir etiqueta
        binding.anadirEtiqueta.setOnClickListener {
            val posicion = binding.listaEtiquetas.selectedItemPosition
            if (listaEtiquetas.isNotEmpty() // Lista vacia protección
                && posicion >= 0 && posicion < listaEtiquetas.size // Protegerse de fuera de limites
                && listaEtiquetas[posicion].id != 0 // Evitar que sea un valor por defecto de no hay etiquetas
                ) {
                // Obtener nueva etiqueta, la lista de etqiuetas y añadirla actualizando las disponibles
                var etiquetaAnadir = listaEtiquetas[posicion]
                var nuevasEtiqeutasDTO = tareaDTO.etiquetas.toMutableList()
                nuevasEtiqeutasDTO.add(etiquetaAnadir)
                tareaDTO.etiquetas = nuevasEtiqeutasDTO
                // Mandamos la lista con las nuevas etiquetas
                adapter.submitList(tareaDTO.etiquetas.toList())
                model.actualizarFiltroListaEtiquetaTareas(tareaDTO.etiquetas)
            }
        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
